package com.example.david.desafioweekeng;

class SingletonClima {
    private static final SingletonClima ourInstance = new SingletonClima();
    private static String  path, typeCityName, configs, appid;
    private String weather;

    static SingletonClima getInstance() {
        return ourInstance;
    }

    private SingletonClima() {
        this.path = "http://api.openweathermap.org/data/2.5/weather?";
        this.typeCityName = "q=";
        this.configs = "&units=metric&appid=";
        this.appid = "2f7345fd89fbfcf13e3d363693dfef27";
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getUrlByCityName(String cityName){
        return (this.path+this.typeCityName+cityName+this.configs+this.appid);
    }
}
