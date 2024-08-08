/*
 * Copyright (c) 2021-present, NoBugLady Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package io.github.nobuglady.network.fw.persistance;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.nobuglady.network.fw.FlowRunner;
import io.github.nobuglady.network.fw.persistance.entity.FlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryEdgeEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryFlowEntity;
import io.github.nobuglady.network.fw.persistance.entity.HistoryNodeEntity;

/**
 * 
 * @author NoBugLady
 *
 */
public class FlowContainer {

	/** key: flowId, historyId */
	public static Map<String, FlowEntity> flowMap = new ConcurrentHashMap<String, FlowEntity>(20);

	/** key: flowId, historyId */
	public static Map<String, FlowRunner> flowRunnerMap = new ConcurrentHashMap<String, FlowRunner>(50);

	/**
	 * 
	 * @return
	 */
	public static String createHistoryId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	public static List<HistoryEdgeEntity> selectEdgeByFlowHistoryId(String flowId, String historyId) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			return flowEntity.edgeEntityList;
		} else {
			return new CopyOnWriteArrayList<HistoryEdgeEntity>();
		}

	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	public static List<HistoryNodeEntity> selectNodeByFlowHistoryId(String flowId, String historyId) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			return flowEntity.nodeEntityList;
		} else {
			return new CopyOnWriteArrayList<HistoryNodeEntity>();
		}
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	public static HistoryFlowEntity selectFlowByKey(String flowId, String historyId) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			return flowEntity.flowEntity;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param flowId
	 * @param nodeId
	 * @param historyId
	 * @return
	 */
	public static HistoryNodeEntity selectNodeByKey(String flowId, String nodeId, String historyId) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					return nodeEntity;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 * @param nodeStatus
	 */
	public static void updateNodeStatusByNodeId(String flowId, String historyId, String nodeId, int nodeStatus) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setNodeStatus(nodeStatus);
				}
			}
		}
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param flowStatus
	 */
	public static void updateFlowStatus(String flowId, String historyId, int flowStatus) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			flowEntity.flowEntity.setFlowStatus(flowStatus);
		}
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeStatus
	 * @return
	 */
	public static List<HistoryNodeEntity> selectNodeListByStatus(String flowId, String historyId, int nodeStatus) {
		List<HistoryNodeEntity> resultList = new CopyOnWriteArrayList<HistoryNodeEntity>();

		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeStatus() == nodeStatus) {
					resultList.add(nodeEntity);
				}
			}
		}

		return resultList;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeStatus
	 * @param nodeStatusDetail
	 * @return
	 */
	public static List<HistoryNodeEntity> selectNodeListByStatusDetail(String flowId, String historyId, int nodeStatus,
			int nodeStatusDetail) {
		List<HistoryNodeEntity> resultList = new CopyOnWriteArrayList<HistoryNodeEntity>();

		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeStatus() == nodeStatus && nodeEntity.getNodeStatusDetail() == nodeStatusDetail) {
					resultList.add(nodeEntity);
				}
			}
		}

		return resultList;
	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @param nodeId
	 * @param nodeStatus
	 * @param nodeStatusDetail
	 */
	public static void updateNodeStatusDetailByNodeId(String flowId, String historyId, String nodeId, int nodeStatus,
			int nodeStatusDetail) {
		String flowKey = getFlowKey(flowId, historyId);
		FlowEntity flowEntity = flowMap.get(flowKey);

		if (flowEntity != null) {
			for (HistoryNodeEntity nodeEntity : flowEntity.nodeEntityList) {
				if (nodeEntity.getNodeId().equals(nodeId)) {
					nodeEntity.setNodeStatus(nodeStatus);
					nodeEntity.setNodeStatusDetail(nodeStatusDetail);
				}
			}
		}
	}

	/**
	 * 
	 * @param flowEntity
	 */
	public static void saveFlow(FlowEntity flowEntity) {
		String flowId = flowEntity.flowEntity.getFlowId();
		String historyId = flowEntity.flowEntity.getHistoryId();

		flowMap.put(getFlowKey(flowId, historyId), flowEntity);

	}

	/**
	 * 
	 * @param flowId
	 * @param historyId
	 * @return
	 */
	private static String getFlowKey(String flowId, String historyId) {
		return flowId + "," + historyId;
	}
}
