package com.navercorp.pinpoint.web.applicationmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.navercorp.pinpoint.web.applicationmap.link.MatcherGroup;
import com.navercorp.pinpoint.web.applicationmap.link.ServerMatcher;
import com.navercorp.pinpoint.web.view.ServerInstanceListSerializer;

/**
 * @author emeroad
 * @author netspider
 * @author minwoo.jung
 */
@JsonSerialize(using = ServerInstanceListSerializer.class)
public class ServerInstanceList {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Map<String, List<ServerInstance>> serverInstanceList = new TreeMap<String, List<ServerInstance>>();
	   
    private MatcherGroup matcherGroup = new MatcherGroup();
	
	public ServerInstanceList() {
	}
	   
    public ServerInstanceList(MatcherGroup matcherGroup) {
        if (matcherGroup != null) {
            this.matcherGroup.addMatcherGroup(matcherGroup);
        }
    }

	public Map<String, List<ServerInstance>> getServerInstanceList() {
		// list의 소트가 안되 있는 문제가 있음.
		return serverInstanceList;
	}

	public int getInstanceCount() {
		int count = 0;
		for (List<ServerInstance> entry : serverInstanceList.values()) {
			count += entry.size();
		}
		return count;
	}

	private void addServerInstance(List<ServerInstance> nodeList, ServerInstance serverInstance) {
		for (ServerInstance node : nodeList) {
			boolean equalsNode = node.equals(serverInstance);
			if (equalsNode) {
				return;
			}
		}
		nodeList.add(serverInstance);
	}

	private List<ServerInstance> getServerInstanceList(String hostName) {
		List<ServerInstance> find = serverInstanceList.get(hostName);
		if (find == null) {
			find = new ArrayList<ServerInstance>();
			serverInstanceList.put(hostName, find);
		}
		return find;
	}

	void addServerInstance(ServerInstance serverInstance) {
		List<ServerInstance> find = getServerInstanceList(serverInstance.getHostName());
		addServerInstance(find, serverInstance);
	}
	
    public Map<String, String> getLink(String serverName) {
        ServerMatcher serverMatcher = matcherGroup.match(serverName);
        
        Map<String, String> linkInfo = new HashMap<String, String>();
        linkInfo.put("linkName", serverMatcher.getLinkName());
        linkInfo.put("linkURL", serverMatcher.getLink(serverName));
        
        return linkInfo;
    }

}
