package cn.microservice.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.microservice.pojo.Order;
import cn.microservice.pojo.OrderDetail;
import cn.microservice.pojo.item;

@Service
public class OrderService {
    private static final Map<String ,Order> MAP=new HashMap<String,Order>();
	static {
		//测试数据
		Order order=new Order();
		order.setOrderId("123455121");
		order.setCreateDate(new Date(0));
		order.setUpdateDate(order.getCreateDate());
		order.setUserId(1L);
		//创建orderDetail集合对象
		List<OrderDetail> orderDetails=new ArrayList<OrderDetail>();
		item Item=new item();
		Item.setId(1L);
		orderDetails.add(new OrderDetail(order.getOrderId(), Item));
		
		Item=new item();
		Item.setId(2L);
		orderDetails.add(new OrderDetail(order.getOrderId(), Item));
		
		order.setOrderDetail(orderDetails);
		
		MAP.put(order.getOrderId(),order);
		
	}
	@Autowired
	private ItemService itemService;
	/**
	 * 根据订单id查询订单数据
	 * @param id
	 * @return
	 */
	public Order queryOrderById(String id)
	{
		Order order=MAP.get(id);
		/**
		 * orderDetail->product id->调用微服务->查询商品信息
		 */
		List<OrderDetail> orderDetail= order.getOrderDetail();
		for(OrderDetail orderdetail:orderDetail)
		{
			Long proId= orderdetail.getItem().getId();
			item Item= itemService.queryItemById(proId);
			orderdetail.setItem(Item);
		}
		return order;
	}
}
