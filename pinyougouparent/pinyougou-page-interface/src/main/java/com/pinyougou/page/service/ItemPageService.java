package com.pinyougou.page.service;

import java.io.IOException;

public interface ItemPageService {
    public Boolean buildHtml(Long goodsId) throws Exception;

    public void deleteHtml(Long id) throws IOException;
}
