## NBP Parser
* The app returns mean of buying rates and standard deviation of selling rates for a given currency and date range.
* Params description:
 * Available currencies: (USD, EUR, CHF, GBP).
 * Date format: yyyy-MM-dd

##### Project setup
    * git clone https://github.com/wojciechzachwieja/parser-nbp.git <project-name>
    * cd <project-nam>
    * mvn clean install
##### To run project
    * java -cp target\parser-nbp-1.0-SNAPSHOT.jar pl.parser.nbp.runn
     er.MainClass <currency-code> <start-date> <end-date>
#### Example
* input `java -cp target\parser-nbp-1.0-SNAPSHOT.jar pl.parser.nbp.runn
             er.MainClass EUR 2015-01-04 2016-01-06`
* output `4,1414 0,0827`
