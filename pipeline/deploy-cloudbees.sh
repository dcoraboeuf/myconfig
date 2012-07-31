#!/bin/bash


# Following environment variables are required
# CB_APPID			CloudBees application ID
# CB_ACCOUNT		CloudBees account name
# CB_JDBC_URL		JDBC URL to the database
# CB_JDBC_USER		JDBC user to the database
# CB_JDBC_PASSWORD	JDBC password to the database

check_env()
{
	if [ "${!1}" == "" ]
	then
		echo Variable $1 must be defined.
		exit 1
	fi
}

check_env "CB_APPID"
check_env "CB_ACCOUNT"
check_env "CB_JDBC_URL"
check_env "CB_JDBC_USER"
check_env "CB_JDBC_PASSWORD"

# Configuration of the application

cb_config_set()
{
	bees config:set --account ${CB_ACCOUNT} --appid ${CB_APPID} "$1"="$2"
	if [ $? -ne 0 ]
	then
		echo Could not configure $1
		exit 1
	fi
}

cb_config_set "myconfig.jdbc.url" "${CB_JDBC_URL}"
cb_config_set "myconfig.jdbc.url" "${CB_JDBC_USER}"
cb_config_set "myconfig.jdbc.url" "${CB_JDBC_PASSWORD}"
cb_config_set "myconfig.appid" "${CB_ACCOUNT}/${CB_APPID}"
cb_config_set "spring.profiles.active" "prod"