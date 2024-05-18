package com.markerhub.search.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(indexName = "shop-product", createIndex = true)
public class EsProductDoc implements Serializable {

	@Id
	private Long id;

	@Field(type = FieldType.Text)
	private String sn;

	@Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
	private String name;

	@Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
	private String keywords;

	@Field(type = FieldType.Text)
	private String image;
	@Field(type = FieldType.Long)
	private Long categoryId;

	@Field(type = FieldType.Auto)
	private BigDecimal price;

	@Field(type = FieldType.Boolean)
	private Boolean isOnSale;

	@Field(type = FieldType.Integer)
	private Integer sortOrder;

	@Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
	private LocalDateTime created;

}
