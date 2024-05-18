package com.markerhub.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.order.entity.AppRefund;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
public interface AppRefundService extends IService<AppRefund> {

	Object getOrderData(long orderId);

	void apply(AppRefund appRefund, MultipartFile[] pics);

	Object pageAdmin(Page page, AppRefund appRefund);

	void auditAdmin(AppRefund appRefund);
}
