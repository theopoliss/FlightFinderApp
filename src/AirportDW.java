public class AirportDW implements AirportInterface {

    public String airportName;
    public String country;

    public AirportDW(String airportName, String country){

        this.country=country;
        this.airportName=airportName;

    }

    public String getAirportName(){
        return airportName;
    }


    public String getCountry(){
        return country;
    }
    @Override
    public String toString() {
        return airportName;
    }

}