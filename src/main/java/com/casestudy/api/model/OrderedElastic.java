package com.casestudy.api.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "orderindex")
@Builder
public class OrderedElastic {

  @Id
  private String id;

  @Field(type = FieldType.Text, name = "product")
  private String product;

  @Field(type = FieldType.Integer, name = "quantity")
  private int quantity;

  @Field(type = FieldType.Boolean, name = "notified")
  private boolean notified;
}
