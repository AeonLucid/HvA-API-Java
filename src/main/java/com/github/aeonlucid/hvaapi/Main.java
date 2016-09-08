package com.github.aeonlucid.hvaapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This should only be used to try out the library during development.
 * Probably have to figure out some better way to do this.
 */
class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            logger.error("Please specify your username and password as arguments.");
            return;
        }

        final HvAClient client = new HvAClient(args[0], args[1]);

        if (!client.signIn()) {
            logger.error("Wrong HvA credentials specified.");
            return;
        }

        logger.info("Successfully authenticated with the Hogeschool van Amsterdam.");

//        logger.info("Domains:");
//        for (Domain domain : client.getDomains()) {
//            logger.info(String.format(" - %s %s", domain.getCode(), domain.getName()));
//        }
//
//        logger.info("Programmes:");
//        for (Programme programme : client.getProgrammes()) {
//            logger.info(String.format(" - %s %s", programme.getName(), programme.getAzUrl()));
//        }
//
//        logger.info("Study Locations:");
//        for (StudyLocation studyLocation : client.getStudyLocations()) {
//            logger.info(String.format(" - %s: %s", studyLocation.getName(), studyLocation.getUrl()));
//        }
//
//        logger.info("News:");
//        for (News news : client.getNews()) {
//            logger.info(String.format(" - %s %s", news.getCreatedBy(), news.getTitle()));
//        }

        if (client.signOut()) {
            logger.info("Successfully signed out.");
        }
    }

}
