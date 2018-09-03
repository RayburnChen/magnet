package org.nfa.atropos.model.thulac;

import java.io.IOException;
import java.util.List;

/**
 * SegPos 分词，结果带有词性.
 */
public class SegPos extends BaseSegmenter<SegItem> {

	public SegPos() throws IOException {
		super(ModelPaths.WEIGHT_PATH, ModelPaths.FEATURE_PATH);
	}

	@Override
	List<SegItem> process(List<SegItem> segItems) {
		return segItems;
	}
}
