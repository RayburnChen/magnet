package org.nfa.lucia.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfig {

	@Bean
	public IndexWriter indexWriter(ConfigProperties configProperties) throws IOException {
		// 创建索引
		Path path = Paths.get(configProperties.getResources().get(ConfigProperties.LUCENE_INDEX_PATH));

		// 指定索引库的存放位置Directory对象
		Directory directory = new MMapDirectory(path);

		// 指定一个标准分析器，对文档内容进行分析
		Analyzer analyzer = new StandardAnalyzer();

		// 创建indexwriterCofig对象
		// 第一个参数： Lucene的版本信息，可以选择对应的lucene版本也可以使用LATEST
		// 第二根参数：分析器对象
		IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);

		// 创建一个indexwriter对象
		return new IndexWriter(directory, writerConfig);
	}

}
