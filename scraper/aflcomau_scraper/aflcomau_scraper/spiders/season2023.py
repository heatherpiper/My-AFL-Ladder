import scrapy


class AflLadderSpider(scrapy.Spider):
    name = 'aflcomau_scraper'
    
    def start_requests(self):
        start_round = 758 # Round 1
        end_round = 781 # Round 24
        base_url = "https://www.afl.com.au/ladder?Competition=1&Season=52&Round={}"

        for round_number in range(start_round, end_round + 1):
            url = base_url.format(round_number)
            yield scrapy.Request(url=url, callback=self.parse, meta={'round_number': round_number})
    
    def parse(self, response):
        for row in response.xpath('//table[@class="stats-table__table"]/tbody/tr[@role="row"]'):
            team = row.xpath('.//th[@class="stats-table__row-header-cell stats-table__cell--club-name"]/div/span[@class="stats-table__club-name"]/text()').get()
            points = row.xpath('.//td[3]/span/text()').get()
            percentage = row.xpath('.//td[4]/text()').get()
            points_for = row.xpath('.//td[8]/text()').get()
            points_against = row.xpath('.//td[9]/text()').get()
        
        yield {
                'team': team,
                'points': points,
                'percentage': percentage,
                'points_for': points_for,
                'points_against': points_against
            }
