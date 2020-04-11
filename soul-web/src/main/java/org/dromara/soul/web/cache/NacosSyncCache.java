package org.dromara.soul.web.cache;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;

import com.alibaba.nacos.api.config.ConfigService;
import com.google.gson.Gson;

/**
 * NacosSyncCache
 * 
 * @author Chenxj
 * @date 2020年3月12日-下午1:49:55
 */
public class NacosSyncCache extends NacosCacheHandler implements CommandLineRunner, DisposableBean {

	public NacosSyncCache(final ConfigService configService) {
		super(configService);
	}

	@Override
	public void run(String... args) throws Exception {
		Gson gson=new Gson();
		watcherData(authDataId,changeData->updateAuthMap(gson, changeData, e->publishConfig(gson, authDataId, AUTH_MAP)));
		watcherData(pluginDataId, changeData->updatePluginMap(gson, changeData, e->publishConfig(gson, pluginDataId, PLUGIN_MAP)));
		watcherData(selectorDataId, changeData->updateSelectorMap(gson, changeData, e->publishConfig(gson, selectorDataId, SELECTOR_MAP)));
		watcherData(metaDataId, changeData->updateMetaDataMap(gson, changeData, e->publishConfig(gson, metaDataId, META_DATA)));
		watcherData(ruleDataId, changeData->updateRuleMap(gson, changeData, e->publishConfig(gson, ruleDataId, RULE_MAP)));
	}
	
	@Override
	public void destroy() throws Exception {
		listeners.forEach((dataId,lss)->{
			lss.stream().forEach(listener->configService.removeListener(dataId, group, listener));
			lss.clear();
		});
		listeners.clear();
	}
}
