package org.jboss.hal.testsuite.fragment;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.WebElement;

/**
 * Implementation of radio input element.
 * Created by mvelas on 24.3.2014.
 */
public class RadioButtonGroup {

    /**
     * All radio buttons relevant to single choice.
     */
    private List<WebElement> choices;

    /**
     * Finds all radio input elements related to choice of given name.
     * @param name name of the value the radio buttons set
     */
    public RadioButtonGroup(String name, WebElement root) {
        ByJQuery selector = ByJQuery.selector("input:radio[name=" + name + "]");
        choices = root.findElements(selector);
    }

    /**
     * @param i index of the radio button
     * @return radio input of i-th choice
     */
    public WebElement getRadioElementByIndex(int i) {
        return choices.get(i);
    }

    /**
     * @param value value of the radio input element
     * @return radio input of given value
     */
    public WebElement getRadioElementByValue(String value) {
        Optional<WebElement> found = choices.stream().filter(radio -> radio.getAttribute("value").equals(value)).findFirst();
        if (!found.isPresent()) {
            throw new NoSuchElementException("Radio input element of value '" + value + "' not found.");
        }
        return found.get();
    }

    /**
     * @param value of the radio input element
     * @return radio input of given value
     */
    public WebElement getRadioElementById(String id) {
        Optional<WebElement> found = choices.stream().filter(radio -> radio.getAttribute("id").equals(id)).findFirst();
        if (!found.isPresent()) {
            throw new NoSuchElementException("Radio input element of id '" + id + "' not found.");
        }
        return found.get();
    }

    /**
     * Picks the radio button.
     * @param index index of the button to select
     */
    public void pickByIndex(int index) {
        WebElement radio = getRadioElementByIndex(index);
        radio.click();
        Graphene.waitGui().until().element(radio).is().selected();
    }

    /**
     * Picks the radio button.
     * @param value of the button to select
     */
    public void pickByValue(String value) {
        WebElement radio = getRadioElementByValue(value);
        radio.click();
        Graphene.waitGui().until().element(radio).is().selected();
    }

    /**
     * Picks the radio button.
     * @param id of the button to select
     */
    public void pickById(String id) {
        WebElement radio = getRadioElementById(id);
        radio.click();
        Graphene.waitGui().until().element(radio).is().selected();
    }

    /**
     * @return index of currently selected radio button
     */
    public int getSelectedIndex() {
        int i = 0;
        for (WebElement choice : choices) {
            if (choice.isSelected()) {
                return i;
            }
            i++;
        }
        return -1;  // nothing found
    }

    /**
     * @return value of currently selected radio button
     */
    public String getValue() {
        return getRadioElementByIndex(getSelectedIndex()).getAttribute("value");
    }
}
