package com.frame.hariko.tx;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

public class MerlinTransactionAttribute extends DefaultTransactionAttribute {

	public MerlinTransactionAttribute() {
		super();
	}

	public MerlinTransactionAttribute(TransactionAttribute ta) {
		super(ta);
	}

	/**
	 * 增加在抛exception异常时也回滚事务
	 * 
	 * @param ex
	 * @return
	 */
	@Override
	public boolean rollbackOn(Throwable ex) {
		return (ex instanceof RuntimeException || ex instanceof Error || ex instanceof Exception);
	}

}
