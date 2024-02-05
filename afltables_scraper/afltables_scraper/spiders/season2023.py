import scrapy

class AFLGamesSpider(scrapy.Spider):
    name = 'afl_games'
    allowed_domains = ['afltables.com']
    start_urls = ['http://afltables.com/afl/seas/2023.html']

    def parse(self, response):
        # Identify each round's section
        rounds = response.xpath('//a[starts-with(@name, "round")]')
        for round in rounds:
            round_number = round.xpath('@name').get()
            # Using round anchor, navigate to game table and iterate through
            game_tables = round.xpath('following-sibling::table[1]//table[@width="100%"]')
            for game_table in game_tables:
                team1, team2 = game_table.xpath('.//tr/td[1]/a/text()').getall()[:2]
                score1, score2 = game_table.xpath('.//tr/td[contains(@width, "5%")]/text()').getall()[:2]
                points_team1, points_team2 = self.determine_result(score1, score2)
                yield {
                    'round': round_number,
                    'team1': team1,
                    'team2': team2,
                    'score1': score1,
                    'score2': score2,
                    'points_team1': points_team1,
                    'points_team2': points_team2,
                }

    # Award 4 points to winning team or 2 points to each team in a draw
    def determine_result(self, score1, score2):
        score1 = int(score1)
        score2 = int(score2)
        
        if score1 == score2:
            points_team1 = points_team2 = 2
        else:
            if score1 > score2:
                points_team1 = 4
                points_team2 = 0
            else:
                points_team1 = 0
                points_team2 = 4
        
        return points_team1, points_team2