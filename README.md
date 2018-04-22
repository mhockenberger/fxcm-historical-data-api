# FXCM Historical Data API

[![GitHub issues](https://img.shields.io/github/issues/mhockenberger/fxcm-historical-data-api.svg)](https://github.com/mhockenberger/fxcm-historical-data-api/issues) [![GitHub stars](https://img.shields.io/github/stars/mhockenberger/fxcm-historical-data-api.svg)](https://github.com/mhockenberger/fxcm-historical-data-api/stargazers)

## Description

**FXCM Historical Data API** is an API designed to access historical prices of financial instruments directly from FXCM.

The following pricing data is available:

* 39 FX pairs including majors and exotics
* CFDs including equities, metals & oil
* 1 / 5 / 15 / 30 min(s), 1 / 4 hour(s), 1 day, 1 week and 1 month data
* Bid / Ask data

## Features

* Login / Logout
* Historical prices of financial instruments request for specified timeframe of the predefined period or last trading day
* If the request is not filled completely, additional request(s) will be sent until completion
* Storage of gathered information

## Arguments

| Argument | Description |
| -------- | ----------- |
| <ul><li>`/login`</li><li>`--login`</li><li>`/l`</li><li>`-l`</li></ul> | Your username. |
| <ul><li>`/password`</li><li>`--password`</li><li>`/p`</li><li>`-p`</li></ul> | Your password. |
| <ul><li>`/url`</li><li>`--url`</li><li>`/u`</li><li>`-u`</li></ul> | The server URL.<br/>For example, [http://www.fxcorporate.com/Hosts.jsp](http://www.fxcorporate.com/Hosts.jsp). |
| <ul><li>`/connection`</li><li>`--connection`</li><li>`/c`</li><li>`-c`</li></ul> | The connection name.<br/>For example, *"Demo"* or *"Real"*. |
| <ul><li>`/sessionid`</li><li>`--sessionid`</li></ul> | The database name.<br/>Required only for users who have accounts in more than one database.<br/>Optional parameter. |
| <ul><li>`/pin`</li><li>`--pin`</li></ul> | Your pin code.<br/>Required only for users who have a pin.<br/>Optional parameter. |
| <ul><li>`/instrument`</li><li>`--instrument`</li><li>`/i`</li><li>`-i`</li></ul> | An instrument which you want to use in sample.<br/>For example, *"GER30"* or *"EUR/USD"*. |
| <ul><li>`/timeframe`</li><li>`--timeframe`</li></ul> | Time period which forms a single candle.<br/>For example, *"t1"* - for 1 tick, *"m1"* - for 1 minute or *"H1"* - for 1 hour. |
| <ul><li>`/datefrom`</li><li>`--datefrom`</li></ul> | DateTime from which you want to receive historical prices.<br/>If you leave this argument as it is, it will mean *datefrom* last trading day.<br/>Format is *"MM.dd.yyyy HH:mm:ss"*.<br/>Optional parameter. |
| <ul><li>`/dateto`</li><li>`--dateto`</li></ul> | DateTime until which you want to receive historical prices.<br/>If you leave this argument as it is, it will mean *dateto* now.<br/>Format is *"MM.dd.yyyy HH:mm:ss"*.<br/>Optional parameter. |
| <ul><li>`/outputdir`</li><li>`--outputdir`</li><li>`/o`</li><li>`-o`</li></ul> | The destination folder for downloaded data. |

## Run configurations

```
Main class: 				de.mhockenberger.fxcmhdapi.core.HistData
VM options: 				-Djava.library.path=libs:libs/java
Program arguments:			-l "{Username}"
					-p "{Password}"
					-u "{URL}"
					-c "{Connection}" 
					-i "{Instrument}"
					--timeframe "{Timeframe}"
					-o "{Output directory}"
Working directory:			./IdeaProjects/FXCM/
Use classpath of module:		FXCM
JRE:					1.8
```

### JAR-Packaged HistData

```
sudo java -Djava.library.path=libs:libs/java -jar artifacts/HistData_jar/HistData.jar
```

## Dev / Requirements / Used SDKs

* [OS X](http://www.apple.com/de/downloads/)
* [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Intellij IDEA](https://www.jetbrains.com/idea/)
* [FXCM *Standard* account](https://www.fxcm.com/de/forex-konto-eroeffnung/)
* [ForexConnectAPI 1.4.1 (*Darwin-universal*)](http://fxcodebase.com/wiki/index.php/Download#Beta_Release_.281.4.1.29)

## Risk disclaimer

The API does not take into consideration your trading objectives, financial situation, needs and level of experience. Therefore it should not be considered as a personal recommendation or investment advice. 

Please seek advice from a separate financial advisor.
