import scrapy

class Season2023Spider(scrapy.Spider):
    name = "season2023"
    allowed_domains = ["afltables.com"]
    start_urls = ["http://afltables.com/afl/seas/2023.html"]

    def parse(self, response):
        # Iterate through each round's section using numeric name attribute in <a> tags
        rounds = response.xpath('//a[@name>0]')
        for round in rounds:
            round_number = round.xpath('./@name').get()
            # Navigate to the first <table> following the round's <a> tag
            game_tables = round.xpath('following-sibling::table[1]')
            for game_table in game_tables:
                team1, team2 = game_table.xpath('.//tr/td[1]/a/text()').getall()[:2]
                score1, score2 = game_table.xpath('.//tr/td[3]/text()').getall()[:2]
                #score1, score2 = scores[0], scores[1]
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
