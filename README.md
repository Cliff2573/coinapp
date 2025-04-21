# coinapp
 Interview Test Project

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
            - request:
                2025-04-15 12:37:15.532 [http-nio-8080-exec-4] INFO  c.c.c.c.c.ControllerLoggingAspect - [144852f660a2] [Controller] 請求方法=CurrencyController.getAll(..)，輸入參數=[0, 20, asc]

            - success:
                2025-04-15 12:37:15.688 [http-nio-8080-exec-4] INFO  c.c.c.c.c.ServiceExceptionAspect - [144852f660a2] [Service] 成功 class=org.springframework.data.repository.PagingAndSortingRepository method=findAll duration=133ms
                2025-04-15 12:37:15.688 [http-nio-8080-exec-4] INFO  c.c.c.c.c.ServiceExceptionAspect - [144852f660a2] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyService method=getAll duration=133ms
                2025-04-15 12:37:15.688 [http-nio-8080-exec-4] INFO  c.c.c.c.c.ControllerLoggingAspect - [144852f660a2] [Controller] 回應方法=CurrencyController.getAll(..)，結果=Page 1 of 1 containing com.cfhtest.coinapp.model.CurrencyModel instances，耗時=156ms

            - error:
                2025-04-15 12:39:52.004 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [ffb928303f55] [Service] 成功 class=org.springframework.data.repository.PagingAndSortingRepository method=findAll duration=164ms
                2025-04-15 12:39:52.004 [http-nio-8080-exec-1] ERROR c.c.c.c.c.ServiceExceptionAspect - [ffb928303f55] [Service] 錯誤 class=com.cfhtest.coinapp.service.CurrencyService method=getAll duration=166ms error=找不到任何貨幣資料
                com.cfhtest.coinapp.core.exception.BusinessException: 找不到任何貨幣資料
                        at com.cfhtest.coinapp.service.CurrencyService.getAll(CurrencyService.java:60)
                        ...
                2025-04-15 12:39:52.015 [http-nio-8080-exec-1] ERROR c.c.c.c.e.GlobalExceptionHandler - [ffb928303f55] BusinessException occurred: 找不到任何貨幣資料

        2. save()
            - request:
                2025-04-15 12:42:03.959 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ControllerLoggingAspect - [a8f3af6dcf4e] [Controller] 請求方法=CurrencyController.save(..)，輸入參數=[CurrencyForm(code=NTD, symbol=NT$, rate=30,123.9876, description=New Taiwan dollar, rateFloat=30123.9876, label=新台幣)]

            - success:
                2025-04-15 12:42:04.137 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [a8f3af6dcf4e] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=save duration=141ms
                2025-04-15 12:42:04.144 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [a8f3af6dcf4e] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=save duration=0ms
                2025-04-15 12:42:04.173 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [a8f3af6dcf4e] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=save duration=29ms
                2025-04-15 12:42:04.174 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [a8f3af6dcf4e] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyService method=save duration=198ms
                2025-04-15 12:42:04.175 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ControllerLoggingAspect - [a8f3af6dcf4e] [Controller] 回應方法=CurrencyController.save(..)，結果=CurrencyModel(code=NTD, symbol=NT$, rate=30,123.9876, description=New Taiwan dollar, rateFloat=30123.9876, label=新台幣, updateDttm=Tue Apr 15 12:42:04 CST 2025)，耗時=216ms

            - error:
                2025-04-15 12:45:31.521 [http-nio-8080-exec-7] ERROR c.c.c.c.e.GlobalExceptionHandler - [4f32a513f44d] Unexpected error occurred: JSON parse error: Cannot deserialize value of type `java.lang.Double` from String "一二三": not a valid `Double` value

        3. getByCode()
            - request:
                2025-04-15 12:46:15.534 [http-nio-8080-exec-8] INFO  c.c.c.c.c.ControllerLoggingAspect - [41e8dd9423a5] [Controller] 請求方法=CurrencyController.getByCode(..)，輸入參數=[USD]

            - success:
                2025-04-15 12:46:15.551 [http-nio-8080-exec-8] INFO  c.c.c.c.c.ServiceExceptionAspect - [41e8dd9423a5] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=findById duration=17ms
                2025-04-15 12:46:15.551 [http-nio-8080-exec-8] INFO  c.c.c.c.c.ServiceExceptionAspect - [41e8dd9423a5] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyService method=getByCode duration=17ms
                2025-04-15 12:46:15.551 [http-nio-8080-exec-8] INFO  c.c.c.c.c.ControllerLoggingAspect - [41e8dd9423a5] [Controller] 回應方法=CurrencyController.getByCode(..)，結果=CurrencyModel(code=USD, symbol=$, rate=23,342.0112, description=US Dollar, rateFloat=23342.0112, label=美金, updateDttm=null)，耗時=17ms

            - error:
                2025-04-15 12:47:20.440 [http-nio-8080-exec-10] INFO  c.c.c.c.c.ServiceExceptionAspect - [f5a591376426] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=findById duration=0ms
                2025-04-15 12:47:20.443 [http-nio-8080-exec-10] ERROR c.c.c.c.c.ServiceExceptionAspect - [f5a591376426] [Service] 錯誤 class=com.cfhtest.coinapp.service.CurrencyService method=getByCode duration=3ms error=找不到指定的貨幣資料：ABC
                com.cfhtest.coinapp.core.exception.BusinessException: 找不到指定的貨幣資料：ABC
                        at com.cfhtest.coinapp.service.CurrencyService.lambda$1(CurrencyService.java:68)
                        ...
                2025-04-15 12:47:20.454 [http-nio-8080-exec-10] ERROR c.c.c.c.e.GlobalExceptionHandler - [f5a591376426] BusinessException occurred: 找不到指定的貨幣資料：ABC

        4. deleteByCode()
            - request:
                2025-04-15 12:50:13.267 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ControllerLoggingAspect - [3116dc015ef8] [Controller] 請求方法=CurrencyController.deleteByCode(..)，輸入參數=[USD]
            
            - success:
                2025-04-15 12:50:13.768 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [3116dc015ef8] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=existsById duration=501ms
                2025-04-15 12:50:13.776 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [3116dc015ef8] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=deleteById duration=8ms
                2025-04-15 12:50:13.776 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ServiceExceptionAspect - [3116dc015ef8] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyService method=deleteByCode duration=509ms
                2025-04-15 12:50:13.781 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ControllerLoggingAspect - [3116dc015ef8] [Controller] 回應方法=CurrencyController.deleteByCode(..)，結果=null，耗時=514ms

            - error:
                2025-04-15 12:51:11.081 [http-nio-8080-exec-2] INFO  c.c.c.c.c.ServiceExceptionAspect - [f88ac1f68064] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=existsById duration=6ms
                2025-04-15 12:51:11.082 [http-nio-8080-exec-2] ERROR c.c.c.c.c.ServiceExceptionAspect - [f88ac1f68064] [Service] 錯誤 class=com.cfhtest.coinapp.service.CurrencyService method=deleteByCode duration=7ms error=找不到指定的貨幣資料，無法刪除：USD
                com.cfhtest.coinapp.core.exception.BusinessException: 找不到指定的貨幣資料，無法刪除：USD
                        at com.cfhtest.coinapp.service.CurrencyService.deleteByCode(CurrencyService.java:114)
                        ...
                2025-04-15 12:51:11.090 [http-nio-8080-exec-2] ERROR c.c.c.c.e.GlobalExceptionHandler - [f88ac1f68064] BusinessException occurred: 找不到指定的貨幣資料，無法刪除：USD

        5. call CoinDesk API:
            - request:
                2025-04-15 12:52:34.843 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ControllerLoggingAspect - [eed0757e5aec] [Controller] 請求方法=CoinDeskController.getCoinDeskData()，輸入參數=[]

            - success:
                2025-04-15 12:52:35.113 [http-nio-8080-exec-3] WARN  c.c.coinapp.service.CoinDeskService - [eed0757e5aec] 無法連接 Coindesk API，使用 Mock Data 代替。
                2025-04-15 12:52:35.122 [http-nio-8080-exec-3] INFO  c.c.coinapp.service.CoinDeskService - [eed0757e5aec] CoinDesk API response: CoinDeskApiResponse(time=CoinDeskApiResponse.Time(updatedISO=2022-08-03T20:25:00+00:00), bpi={USD=CoinDeskApiResponse.Currency(code=USD, rate=23,342.0112, rate_float=23342.0112), GBP=CoinDeskApiResponse.Currency(code=GBP, rate=19,504.3978, rate_float=19504.3978), EUR=CoinDeskApiResponse.Currency(code=EUR, rate=22,738.5269, rate_float=22738.5269)})
                2025-04-15 12:52:35.127 [http-nio-8080-exec-3] INFO  c.c.coinapp.service.CoinDeskService - [eed0757e5aec] 幣別: USD
                2025-04-15 12:52:35.129 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=findById duration=2ms
                2025-04-15 12:52:35.131 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyLabelService method=getCurrencyLabel duration=3ms
                2025-04-15 12:52:35.131 [http-nio-8080-exec-3] INFO  c.c.coinapp.service.CoinDeskService - [eed0757e5aec] 幣別: GBP
                2025-04-15 12:52:35.133 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=findById duration=2ms
                2025-04-15 12:52:35.133 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyLabelService method=getCurrencyLabel duration=2ms
                2025-04-15 12:52:35.133 [http-nio-8080-exec-3] INFO  c.c.coinapp.service.CoinDeskService - [eed0757e5aec] 幣別: EUR
                2025-04-15 12:52:35.134 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=org.springframework.data.repository.CrudRepository method=findById duration=1ms
                2025-04-15 12:52:35.134 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=com.cfhtest.coinapp.service.CurrencyLabelService method=getCurrencyLabel duration=1ms
                2025-04-15 12:52:35.134 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ServiceExceptionAspect - [eed0757e5aec] [Service] 成功 class=com.cfhtest.coinapp.service.CoinDeskService method=parseCoinDeskData duration=289ms
                2025-04-15 12:52:35.134 [http-nio-8080-exec-3] INFO  c.c.c.c.c.ControllerLoggingAspect - [eed0757e5aec] [Controller] 回應方法=CoinDeskController.getCoinDeskData()，結果=CoinDeskModel(updateTime=2022/08/03 20:25:00, currencies=[CoinDeskModel.CurrencyInfo(code=USD, label=美金, rate=23,342.0112, rateFloat=23342.0112), CoinDeskModel.CurrencyInfo(code=GBP, label=英鎊, rate=19,504.3978, rateFloat=19504.3978), CoinDeskModel.CurrencyInfo(code=EUR, label=歐元, rate=22,738.5269, rateFloat=22738.5269)])，耗時=291ms

            - error:
                2025-04-15 12:59:08.049 [http-nio-8080-exec-1] WARN  c.c.coinapp.service.CoinDeskService - [5f5f4945b5c8] 無法連接 Coindesk API，使用 Mock Data 代替。
                2025-04-15 12:59:08.089 [http-nio-8080-exec-1] INFO  c.c.coinapp.service.CoinDeskService - [5f5f4945b5c8] CoinDesk API response: CoinDeskApiResponse(time=CoinDeskApiResponse.Time(updatedISO=2022-08-03T20:25:00+00:00), bpi={USD=CoinDeskApiResponse.Currency(code=USD, rate=23,342.0112, rate_float=23342.0112), GBP=CoinDeskApiResponse.Currency(code=GBP, rate=19,504.3978, rate_float=19504.3978), EUR=CoinDeskApiResponse.Currency(code=EUR, rate=22,738.5269, rate_float=22738.5269)})
                2025-04-15 12:59:08.096 [http-nio-8080-exec-1] ERROR c.c.c.c.c.ServiceExceptionAspect - [5f5f4945b5c8] [Service] 錯誤 class=com.cfhtest.coinapp.service.CoinDeskService method=parseCoinDeskData duration=226ms error=解析 CoinDesk API 資料時發生錯誤：com.cfhtest.coinapp.core.exception.BusinessException: 測試錯誤
                com.cfhtest.coinapp.core.exception.BusinessException: 解析 CoinDesk API 資料時發生錯誤：com.cfhtest.coinapp.core.exception.BusinessException: 測試錯誤
                        at com.cfhtest.coinapp.service.CoinDeskService.parseCoinDeskData(CoinDeskService.java:156)
                        ...
                2025-04-15 12:59:08.108 [http-nio-8080-exec-1] ERROR c.c.c.controller.CoinDeskController - [5f5f4945b5c8] Error while processing CoinDesk API data: 解析 CoinDesk API 資料時發生錯誤：com.cfhtest.coinapp.core.exception.BusinessException: 測試錯誤
                2025-04-15 12:59:08.109 [http-nio-8080-exec-1] INFO  c.c.c.c.c.ControllerLoggingAspect - [5f5f4945b5c8] [Controller] 回應方法=CoinDeskController.getCoinDeskData()，結果=null，耗時=241ms

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
