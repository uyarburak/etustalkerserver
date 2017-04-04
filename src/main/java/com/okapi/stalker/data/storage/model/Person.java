package com.okapi.stalker.data.storage.model;

import com.okapi.stalker.data.storage.model.Section;

import java.util.Set;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Created by burak on 6/12/2016.
 */
public interface Person {
    public Set<Section> getSections();
}
