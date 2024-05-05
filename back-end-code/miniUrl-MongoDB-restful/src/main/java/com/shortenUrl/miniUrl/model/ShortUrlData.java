package com.shortenUrl.miniUrl.model;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.RepresentationModel;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;




import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;



@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class ShortUrlData
{

  @Id
  public String id;


  
	@NotNull(message = "You need to enter a url to be shorten")
  @Size(min = 10, max = 10000)
  private String longUrl;


  private String shortUrl;

  private Integer shortenTimes;

  private Integer accessTimes;

  private String belongUser;



}