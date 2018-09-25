package org.nfa.atropos.model.thulac;

import java.io.Serializable;
import java.util.List;

/**
 * 词黏结.
 */
public interface Cementer extends Serializable {

    /**
     * 黏结分词结果.
     *
     * @param segItems 序列标注结果.
     */
    void cement(List<SegItem> segItems);
}
