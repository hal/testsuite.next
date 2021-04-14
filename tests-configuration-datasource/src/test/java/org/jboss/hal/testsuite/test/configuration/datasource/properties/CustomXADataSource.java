package org.jboss.hal.testsuite.test.configuration.datasource.properties;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

public class CustomXADataSource implements XADataSource, Referenceable {

    private String propertyOne = "one";
    private String propertyTwo = "two";
    private boolean booleanProperty = true;
    private int integerProperty = 1000;

    @Override
    public XAConnection getXAConnection() throws SQLException {
        return null;
    }

    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public void setPropertyOne(String propertyOne) {
        this.propertyOne = propertyOne;
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    public void setPropertyTwo(String propertyTwo) {
        this.propertyTwo = propertyTwo;
    }

    public String getPropertyTwo() {
        return propertyTwo;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty = integerProperty;
    }

    public int getIntegerProperty() {
        return integerProperty;
    }

    @Override
    public Reference getReference() {
        return new Reference(CustomXADataSource.class.getName());
    }
}
