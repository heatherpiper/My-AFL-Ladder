import scrapy


class Season2023Spider(scrapy.Spider):
    name = "season2023"
    allowed_domains = ["afltables.com"]
    start_urls = ["https://afltables.com/afl/seas/2023.html"]

    def parse(self, response):
        return {
            #'author': response.xpath('//span[@class="author-name"]/text()').get(),
            #'author': response.xpath('//meta[@name="DC.Creator"]/@content').get(),
            'title': response.xpath('//title/text()').get()
        }
        pass