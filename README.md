# coinapp
 Cathy Financial Holdings Test Project

Basic:
 * Spring-boot, Java 17, H2 DB
 * RESTful API.
 * H2 Virtual MySQL DB.
 * Call Coindesk API.
 * Scheduled: 
        CoinDeskService.updateCurrencyRates()
 * Unit Test. 

Bonus:
 * AOP:
        1. getAll()
            - response body :
                {
                    "success": true,
                    "data": {
                        "content": [
                            {
                                "code": "EUR",
                                "symbol": "€",
                                "rate": "22,738.5269",
                                "description": "Euro",
                                "label": "歐元",
                                "updateDttm": null,
                                "rate_float": 22738.5269
                            },
                            {
                                "code": "GBP",
                                "symbol": "£",
                                "rate": "19,504.3978",
                                "description": "British Pound Sterling",
                                "label": "英鎊",
                                "updateDttm": null,
                                "rate_float": 19504.3978
                            },
                            {
                                "code": "USD",
                                "symbol": "$",
                                "rate": "23,342.0112",
                                "description": "US Dollar",
                                "label": "美金",
                                "updateDttm": null,
                                "rate_float": 23342.0112
                            }
                        ],
                        "pageable": {
                            "pageNumber": 0,
                            "pageSize": 20,
                            "sort": {
                                "empty": false,
                                "sorted": true,
                                "unsorted": false
                            },
                            "offset": 0,
                            "paged": true,
                            "unpaged": false
                        },
                        "last": true,
                        "totalPages": 1,
                        "totalElements": 3,
                        "first": true,
                        "size": 20,
                        "number": 0,
                        "sort": {
                            "empty": false,
                            "sorted": true,
                            "unsorted": false
                        },
                        "numberOfElements": 3,
                        "empty": false
                    },
                    "error": null
                }

        2. save()
            - request body:
                {
                    "code": "NTD",
                    "symbol": "NT$",
                    "rate": "30,123.9876",
                    "description": "New Taiwan dollar",
                    "label": "新台幣",
                    "rate_float": 30123.9876
                }
            - response body:
                {
                    "success": true,
                    "data": {
                        "code": "NTD",
                        "symbol": "NT$",
                        "rate": "30,123.9876",
                        "description": "New Taiwan dollar",
                        "label": "新台幣",
                        "updateDttm": "2025-04-07T16:33:05.029+00:00",
                        "rate_float": 30123.9876
                    },
                    "error": null
                }
            - error:
                {
                    "success": true,
                    "data": {
                        "success": false,
                        "data": null,
                        "error": "代碼不得為空"
                    },
                    "error": null
                }

        3. getByCode()
            - response body:
                {
                    "success": true,
                    "data": {
                        "code": "USD",
                        "symbol": "$",
                        "rate": "23,342.0112",
                        "description": "US Dollar",
                        "label": "美金",
                        "updateDttm": null,
                        "rate_float": 23342.0112
                    },
                    "error": null
                }
            - error:
                {
                    "success": true,
                    "data": {
                        "success": false,
                        "data": null,
                        "error": "找不到指定的貨幣資料：ABC"
                    },
                    "error": null
                }

        4. deleteByCode()
            - response body:
                {
                    "success": true,
                    "data": null,
                    "error": null
                }
            - error:
                {
                    "success": true,
                    "data": {
                        "success": false,
                        "data": null,
                        "error": "找不到指定的貨幣資料，無法刪除：ABC"
                    },
                    "error": null
                }

        5. call CoinDesk API:
            - response body:
                {
                    "success": true,
                    "data": {
                        "updateTime": "2022/08/03 20:25:00",
                        "currencies": [
                        {
                            "code": "USD",
                            "label": "美金",
                            "rate": "23,342.0112",
                            "rate_float": 23342.0112
                        },
                        {
                            "code": "GBP",
                            "label": "英鎊",
                            "rate": "19,504.3978",
                            "rate_float": 19504.3978
                        },
                        {
                            "code": "EUR",
                            "label": "歐元",
                            "rate": "22,738.5269",
                            "rate_float": 22738.5269
                        }
                        ]
                    },
                    "error": null
                }
            - error:
                {
                    "success": true,
                    "data": {
                        "success": false,
                        "data": null,
                        "error": "解析 CoinDesk API 資料時發生錯誤"
                    },
                    "error": null
                }

 * Swagger-ui: 
        http://localhost:8080/swagger-ui/index.html
 * i18n: 
        messages_zh.properties, messages_en.properties
 * Design patterns:
        1. Repository Pattern
        2. Service Layer Pattern
        3. Compound Pattern (Controller in MVC)
        * Other design patterns in Spring 
 * Docker:
        1. docker-composer.yml, Dockerfile;
        2. prd: docker-compose up --build -d
        3. dev: docker build -t coinapp_dev .
                docker run -d -p 8080:8080 --name coinapp_dev coinapp_dev