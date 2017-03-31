package com.github.wangyi.activemq.model;

import java.io.Serializable;
import java.util.Date;

public class OrderInfo implements Serializable{
	
	private static final long serialVersionUID = 1752860849786910450L;
	
	/**商品ID*/
	private int goodsId;
	
	/**商品名称*/
	private String goods;
	
	/**商品价格*/
	private float price;
	
	/**订单时间*/
	private Date createTime;

	@Override
	public String toString() {
		return "OrderInfo [goodsId=" + goodsId + ", goods=" + goods
				+ ", price=" + price + ", createTime=" + createTime + "]";
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
