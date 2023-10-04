public class AirportAE implements AirportInterface {
    private String airportName;
    private String country;
    public AirportAE(String airportName, String country) {
        this.airportName= airportName;
        this.country = country;
    }
    @Override
    public String getAirportName() {
        return this.airportName;
    }

    @Override
    public String getCountry() {
        return this.country;
    }
    @Override
    public String toString() {
        return airportName + " (" + country + ")";
    }
}
