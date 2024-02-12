import scrapy


class AfltablesSeason2023Spider(scrapy.Spider):
    name = "afltables_season2023"
    allowed_domains = ["afltables.com"]
    start_urls = ["https://afltables.com/afl/seas/2023.html"]

    def parse(self, response):
        for table in response.xpath('//table'):
            title = table.xpath('.//tr[1]/td/text()').get()
            if title and title.startswith('Rd') and 'Ladder' in title:
                for row in table.xpath('.//tr[position()>1]'):
                    team = row.xpath('.//td[1]/text()').get()
                    games_played = row.xpath('.//td[2]/text()').get()
                    total_points = row.xpath('.//td[3]/text()').get()
                    score_differential = row.xpath('.//td[4]/text()').get()
                    yield {
                        'round': title,
                        'team': team,
                        'games_played': games_played.strip(),
                        'total_points': total_points.strip(),
                        'score_differential': score_differential.strip()
                    }