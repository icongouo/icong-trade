package com.shangan.trade.goods.service;

public interface GoodsTranscationService {
    boolean tryGoodsStock();

    boolean cancleGoodsStock();

    boolean commitGoodsStock();
}
