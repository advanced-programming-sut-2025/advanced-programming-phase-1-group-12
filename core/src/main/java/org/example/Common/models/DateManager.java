package org.example.Common.models;

import org.example.Common.models.enums.Season;
import org.example.Common.models.enums.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Singleton Date Manager for multiplayer game synchronization
 * Ensures all players share the same game time and date
 */
public class DateManager {
    private static final Logger logger = LoggerFactory.getLogger(DateManager.class);
    private static DateManager instance;
    private static final Object lock = new Object();
    
    private Date gameDate;
    private boolean isInitialized = false;
    
    private DateManager() {
        // Private constructor to prevent instantiation
    }
    
    public static DateManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DateManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize the global game date (should be called once per game session)
     */
    public void initializeGameDate() {
        synchronized (lock) {
            if (!isInitialized) {
                gameDate = new Date();
                isInitialized = true;
                logger.info("Game date initialized: {}h, Day {}, {}, Year {}", 
                    gameDate.getHour(), gameDate.getDayOfMonth(), 
                    gameDate.getSeason(), gameDate.getYear());
            }
        }
    }
    
    /**
     * Initialize with specific date values (for loading saved games or server sync)
     */
    public void initializeGameDate(int hour, int dayOfMonth, int dayOfWeek, 
                                 Season season, int year, Weather weather) {
        synchronized (lock) {
            gameDate = new Date();
            gameDate.setHour(hour);
            gameDate.setDayOfMonth(dayOfMonth);
            gameDate.setDayOfWeek(dayOfWeek);
            gameDate.setSeason(season);
            // Note: Date class doesn't have setYear method, year is set in constructor
            gameDate.setWeather(weather);
            isInitialized = true;
            
            logger.info("Game date initialized with custom values: {}h, Day {}, {}, Year {}", 
                hour, dayOfMonth, season, gameDate.getYear());
        }
    }
    
    /**
     * Get the shared game date instance
     */
    public Date getGameDate() {
        if (!isInitialized) {
            initializeGameDate();
        }
        return gameDate;
    }
    
    /**
     * Reset the date manager (for new games)
     */
    public void reset() {
        synchronized (lock) {
            if (gameDate != null) {
                gameDate.stop(); // Stop the existing thread
            }
            gameDate = null;
            isInitialized = false;
            logger.info("Date manager reset");
        }
    }
    
    /**
     * Check if the date manager is initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    // Convenience methods for accessing date properties
    public int getHour() {
        return getGameDate().getHour();
    }
    
    public int getDayOfMonth() {
        return getGameDate().getDayOfMonth();
    }
    
    public int getDayOfWeek() {
        return getGameDate().getDayOfWeek();
    }
    
    public Season getSeason() {
        return getGameDate().getSeason();
    }
    
    public int getYear() {
        return getGameDate().getYear();
    }
    
    public Weather getWeather() {
        return getGameDate().getWeather();
    }
    
    public Weather getTomorrowWeather() {
        return getGameDate().getTommorowWeather();
    }
    
    /**
     * Advance time by specified hours (synchronized across all players)
     */
    public void advanceTime(int hours) {
        synchronized (lock) {
            if (isInitialized && gameDate != null) {
                gameDate.changeAdvancedTime(hours);
                logger.debug("Advanced time by {} hours. Current time: {}h, Day {}", 
                    hours, gameDate.getHour(), gameDate.getDayOfMonth());
            }
        }
    }
    
    /**
     * Advance to next day (synchronized across all players)
     */
    public void advanceDay(int days) {
        synchronized (lock) {
            if (isInitialized && gameDate != null) {
                gameDate.changeAdvancedDay(days);
                logger.debug("Advanced {} days. Current: Day {}, {}, Year {}", 
                    days, gameDate.getDayOfMonth(), gameDate.getSeason(), gameDate.getYear());
            }
        }
    }
}