<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Regalia - Network Monitoring</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/jstree/dist/themes/default/style.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #1a1a2e; color: #eee; height: 100vh; display: flex; flex-direction: column; }
        #header { background: #16213e; padding: 12px 24px; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #0f3460; flex-shrink: 0; }
        #header h1 { font-size: 20px; color: #e94560; letter-spacing: 2px; cursor: pointer; }
        .status-summary { display: flex; gap: 16px; font-size: 13px; }
        .badge { padding: 4px 12px; border-radius: 12px; font-weight: bold; }
        .badge.green  { background: #1a4a1a; color: #4caf50; }
        .badge.yellow { background: #4a3a00; color: #ffeb3b; }
        .badge.red    { background: #4a1a1a; color: #f44336; }
        #main { display: flex; flex: 1; overflow: hidden; }
        #sidebar { width: 240px; background: #16213e; border-right: 1px solid #0f3460; display: flex; flex-direction: column; flex-shrink: 0; }
        .sidebar-title { padding: 12px 16px; font-size: 12px; color: #888; text-transform: uppercase; letter-spacing: 1px; border-bottom: 1px solid #0f3460; }
        #device-list { flex: 1; overflow-y: auto; padding: 8px; }
        .jstree-default .jstree-wholerow-hovered { background: #0f3460 !important; }
        .jstree-default .jstree-wholerow-clicked { background: #1a3a6e !important; }
        .jstree-default .jstree-hovered { background: transparent !important; box-shadow: none !important; }
        .jstree-default .jstree-clicked { background: transparent !important; box-shadow: none !important; }
        .jstree-default .jstree-anchor { color: #eee !important; }
		
		/* 닫혀있을 때 */
		.jstree-default .jstree-closed > .jstree-ocl {
		    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 10 10'%3E%3Cpath d='M3 2 L7 5 L3 8' fill='none' stroke='%23888888' stroke-width='2'/%3E%3C/svg%3E") !important;
		    background-position: center !important;
		    background-repeat: no-repeat !important;
		    background-size: 10px !important;
		}

		/* 열려있을 때 */
		.jstree-default .jstree-open > .jstree-ocl {
		    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 10 10'%3E%3Cpath d='M2 3 L5 7 L8 3' fill='none' stroke='%23ffffff' stroke-width='2'/%3E%3C/svg%3E") !important;
		    background-position: center !important;
		    background-repeat: no-repeat !important;
		    background-size: 10px !important;
		}
		/* 장비 상태별 색깔 */
		.jstree-default [class*="device-green"] .jstree-anchor { color: #4caf50 !important; }
		.jstree-default [class*="device-yellow"] .jstree-anchor { color: #ffeb3b !important; }
		.jstree-default [class*="device-red"] .jstree-anchor { color: #f44336 !important; }
        .sidebar-btns { padding: 12px; display: flex; flex-direction: column; gap: 8px; border-top: 1px solid #0f3460; }
        .btn { padding: 8px; border: none; border-radius: 6px; cursor: pointer; font-size: 13px; width: 100%; }
        .btn-primary { background: #e94560; color: white; }
        .btn-secondary { background: #0f3460; color: #eee; }
        .btn:hover { opacity: 0.85; }
        #topology { flex: 1; background: #0d1117; position: relative; }
        #topology-canvas { width: 100%; height: 100%; }
        #detail-panel { width: 340px; background: #16213e; border-left: 1px solid #0f3460; display: flex; flex-direction: column; flex-shrink: 0; transition: width 0.3s; overflow: hidden; }
        #detail-panel.hidden { width: 0; }
        .panel-header { padding: 14px 16px; border-bottom: 1px solid #0f3460; display: flex; justify-content: space-between; align-items: center; flex-shrink: 0; }
        .panel-header h3 { font-size: 15px; }
        .btn-close { background: none; border: none; color: #888; cursor: pointer; font-size: 18px; }
        .panel-section { padding: 14px 16px; border-bottom: 1px solid #0f3460; flex-shrink: 0; }
        .panel-section h4 { font-size: 12px; color: #888; margin-bottom: 10px; text-transform: uppercase; }
        #response-chart-wrap { position: relative; height: 160px; }
        #log-list { flex: 1; overflow-y: auto; padding: 8px 16px; }
        .log-item { padding: 6px 0; font-size: 12px; border-bottom: 1px solid #0f3460; display: flex; gap: 8px; align-items: center; }
        .log-item .time  { color: #888; flex-shrink: 0; }
        .log-item .ms    { flex-shrink: 0; }
        .log-item .GREEN  { color: #4caf50; }
        .log-item .YELLOW { color: #ffeb3b; }
        .log-item .RED    { color: #f44336; }
        .modal-overlay { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 100; align-items: center; justify-content: center; }
        .modal-overlay.show { display: flex; }
        .modal { background: #16213e; border: 1px solid #0f3460; border-radius: 10px; padding: 24px; width: 380px; }
        .modal h3 { margin-bottom: 16px; font-size: 16px; }
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-size: 12px; color: #888; margin-bottom: 6px; }
        .form-group input, .form-group select { width: 100%; padding: 8px 12px; background: #0d1117; border: 1px solid #0f3460; border-radius: 6px; color: #eee; font-size: 13px; }
        .modal-btns { display: flex; gap: 8px; justify-content: flex-end; margin-top: 16px; }
        .modal-btns .btn { width: auto; padding: 8px 20px; }
    </style>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free/css/all.min.css">
</head>
<body>

    <div id="header">
        <h1 onclick="window.location.reload()">REGALIA</h1>
        <div class="status-summary">
            <span class="badge green">GREEN <span id="cnt-green">0</span></span>
            <span class="badge yellow">YELLOW <span id="cnt-yellow">0</span></span>
            <span class="badge red">RED <span id="cnt-red">0</span></span>
        </div>
    </div>

    <div id="main">
        <div id="sidebar">
            <div class="sidebar-title">장비 목록</div>
            <div id="device-list"></div>
            <div class="sidebar-btns">
                <button class="btn btn-primary" onclick="openAddDeviceModal()">+ 장비 추가</button>
                <button class="btn btn-secondary" onclick="openAddGroupModal()">+ 그룹 추가</button>
            </div>
        </div>

        <div id="topology">
            <div id="topology-canvas"></div>
        </div>

        <div id="detail-panel" class="hidden">
            <div class="panel-header">
                <h3 id="panel-device-name">장비명</h3>
                <button class="btn-close" onclick="closePanel()">X</button>
            </div>
            <div class="panel-section">
                <h4>응답시간 그래프</h4>
                <div id="response-chart-wrap">
                    <canvas id="response-chart"></canvas>
                </div>
            </div>
            <div class="panel-section">
                <h4>최신 로그</h4>
            </div>
            <div id="log-list"></div>
        </div>
    </div>

    <!-- 장비 추가/수정 모달 -->
    <div id="modal-add-device" class="modal-overlay">
        <div class="modal">
            <h3 id="modal-device-title">장비 추가</h3>
            <div class="form-group">
                <label>장비명</label>
                <input type="text" id="input-device-name" placeholder="서버명 입력">
            </div>
            <div class="form-group">
                <label>URL / IP</label>
                <input type="text" id="input-device-url" placeholder="http://192.168.0.1">
            </div>
            <div class="form-group">
                <label>그룹</label>
                <select id="input-device-group">
                    <option value="">미분류</option>
                </select>
            </div>
            <div class="form-group">
                <label>설명</label>
                <input type="text" id="input-device-desc" placeholder="설명 입력 (선택)">
            </div>
            <div class="modal-btns">
                <button class="btn btn-secondary" onclick="closeDeviceModal()">취소</button>
                <button class="btn btn-primary" id="btn-device-save" onclick="saveDevice()">추가</button>
            </div>
        </div>
    </div>

    <!-- 그룹 추가/수정 모달 -->
    <div id="modal-add-group" class="modal-overlay">
        <div class="modal">
            <h3 id="modal-group-title">그룹 추가</h3>
            <div class="form-group">
                <label>그룹명</label>
                <input type="text" id="input-group-name" placeholder="그룹명 입력">
            </div>
            <div class="form-group">
                <label>설명</label>
                <input type="text" id="input-group-desc" placeholder="설명 입력 (선택)">
            </div>
            <div class="modal-btns">
                <button class="btn btn-secondary" onclick="closeGroupModal()">취소</button>
                <button class="btn btn-primary" id="btn-group-save" onclick="saveGroup()">추가</button>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/jquery/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jstree/dist/jstree.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vis-network/standalone/umd/vis-network.min.js"></script>

    <script>
        let responseChart = null;
        let currentDeviceId = null;  // 수정 시 사용할 장비 id
        let currentGroupId  = null;  // 수정 시 사용할 그룹 id

        // =====================
        // 초기화
        // =====================
        document.addEventListener('DOMContentLoaded', function() {
            loadTree();
			loadTopology();
        });

        // =====================
        // 트리 로드
        // =====================
        function loadTree() {
            fetch('/api/tree')
                .then(res => res.json())
                .then(data => {
                    $('#device-list').jstree('destroy');
                    $('#device-list').jstree({
                        core: { data: data, check_callback: true },
						types: {
						    root:   { icon: 'fa-solid fa-bars-staggered' },
							group:          { icon: 'fa fa-folder' },
						    'device-green':  { icon: 'fa fa-server' },
						    'device-yellow': { icon: 'fa fa-server' },
						    'device-red':    { icon: 'fa fa-server' },
							default : {
								max_depth : 2
							}
						},
						contextmenu: {
						    items: function(node) {
						        if (node.type === 'root') {
						            return {};  // 루트는 메뉴 없음
						        } else if (node.type === 'group') {
						            return {
						                rename: { label: '그룹 수정', action: function() { openEditGroupModal(node); } },
						                remove: { label: '그룹 삭제', action: function() { deleteGroup(node); } }
						            };
						        } else if (node.type.startsWith('device')) {
						            return {
						                rename: { label: '장비 수정', action: function() { openEditDeviceModal(node); } },
						                remove: { label: '장비 삭제', action: function() { deleteDevice(node); } }
						            };
						        }
						    }
						},
                        plugins: ['contextmenu', 'types', 'dnd', 'wholerow']
                    });

                    $('#device-list').on('select_node.jstree', function(e, obj) {
                        if (obj.node.type.startsWith('device')) {
                            var deviceId = obj.node.id.replace('device_', '');
                            openDetailPanel(deviceId, obj.node.text);
                        }
                    });
					
					$('#device-list').on('ready.jstree', function() {
					    var instance = $(this).jstree(true);
					    // 루트를 열면 자동으로 하위 노드들이 렌더링되면서 색상 계산 시작
					    instance.open_node('root');
					});

					// 노드가 실제로 열린 직후(DOM 생성 완료) 호출
					$('#device-list').on('after_open.jstree', function (e, data) {
					    applyStatusColors();
					});
					
					$('#device-list').on('open_node.jstree', function() {
					    applyStatusColors();
					});

					// 드래그앤드롭으로 장비 이동 시
					$('#device-list').on('move_node.jstree', function(e, data) {
					    if (data.node.type.startsWith('device')) {
					        var deviceId = data.node.id.replace('device_', '');
					        var newParentId = data.parent;
					        var body = { id: parseInt(deviceId) };

					        // 부모가 root거나 #이면 미분류
					        if (newParentId === 'root' || newParentId === '#') {
					            body.group = null;
					        } else {
					            body.group = { id: parseInt(newParentId.replace('group_', '')) };
					        }

					        fetch('/api/devices/' + deviceId, {
					            method: 'PUT',
					            headers: { 'Content-Type': 'application/json' },
					            body: JSON.stringify(body)
					        })
					        .then(function() {
					            //loadTree();*/
								
					        });
							applyStatusColors();
					    }
					});

                    updateStatusBadge(data);
                });
        }
		
		function applyStatusColors() {
		    var instance = $('#device-list').jstree(true);
		    if (!instance) return;

		    var groupStatus = {};

		    // 1. DOM 중심이 아니라 jstree의 전체 '데이터'를 순회
		    var allNodes = instance._model.data; 

		    Object.keys(allNodes).forEach(function(id) {
		        var node = allNodes[id];
		        if (id === '#' || !node.type) return;

		        // 장비 노드인 경우 부모 그룹의 상태를 결정
		        if (node.type.startsWith('device')) {
		            var parentId = node.parent;
		            if (!groupStatus[parentId]) groupStatus[parentId] = 0;

		            if (node.type === 'device-red') {
		                groupStatus[parentId] = 2; // RED가 하나라도 있으면 RED
		            } else if (node.type === 'device-yellow' && groupStatus[parentId] < 2) {
		                groupStatus[parentId] = 1; // YELLOW
		            }
		            
		            // 장비 자체 아이콘 색상 입히기 (DOM이 존재할 때만)
		            var $nodeEl = $('#' + id);
		            if ($nodeEl.length) {
		                var color = (node.type === 'device-red') ? '#f44336' : 
		                            (node.type === 'device-yellow') ? '#ffeb3b' : '#4caf50';
		                $nodeEl.find('> .jstree-anchor > .jstree-themeicon').css('color', color);
		            }
		        }
		    });

		    // 2. 수집된 상태로 그룹 아이콘 색상 적용
		    Object.keys(groupStatus).forEach(function(groupId) {
		        var stat = groupStatus[groupId];
		        var color = (stat === 2) ? '#f44336' : (stat === 1) ? '#ffeb3b' : '#4caf50';
		        
		        // 그룹 아이콘 색상 강제 적용
		        $('#' + groupId).find('> .jstree-anchor > .jstree-themeicon').css('color', color);
		    });

		    // 루트(REGALIA)는 항상 기본색 유지
		    $('#root').find('> .jstree-anchor > .jstree-themeicon').css('color', '#eee');
		}

        // =====================
        // 상태 뱃지 업데이트
        // =====================
		function updateStatusBadge(treeData) {
		    var green = 0, yellow = 0, red = 0;

		    // treeData[0]이 REGALIA 루트
		    var rootChildren = treeData[0].children || [];
		    rootChildren.forEach(function(node) {
		        if (node.type === 'group' && node.children) {
		            node.children.forEach(function(device) {
		                if (device.status === 'GREEN')  green++;
		                if (device.status === 'YELLOW') yellow++;
		                if (device.status === 'RED')    red++;
		            });
		        } else if (node.type.startsWith('device')) {
		            if (node.status === 'GREEN')  green++;
		            if (node.status === 'YELLOW') yellow++;
		            if (node.status === 'RED')    red++;
		        }
		    });

		    document.getElementById('cnt-green').textContent  = green;
		    document.getElementById('cnt-yellow').textContent = yellow;
		    document.getElementById('cnt-red').textContent    = red;
		}

        // =====================
        // 상세 패널
        // =====================
        function openDetailPanel(deviceId, deviceName) {
            document.getElementById('panel-device-name').textContent = deviceName;
            document.getElementById('detail-panel').classList.remove('hidden');
            loadGraph(deviceId);
            loadLogs(deviceId);
        }

        function closePanel() {
            document.getElementById('detail-panel').classList.add('hidden');
        }

        // =====================
        // 그래프
        // =====================
        function loadGraph(deviceId) {
            fetch('/api/logs/' + deviceId + '/graph')
                .then(res => res.json())
                .then(function(logs) {
                    var labels = logs.map(function(l) { return l.checkedAt ? l.checkedAt.substring(11, 16) : ''; });
                    var data   = logs.map(function(l) { return l.responseTime || 0; });

                    if (responseChart) responseChart.destroy();
                    var ctx = document.getElementById('response-chart').getContext('2d');
                    responseChart = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: '응답시간 (ms)',
                                data: data,
                                borderColor: '#e94560',
                                backgroundColor: 'rgba(233,69,96,0.1)',
                                tension: 0.3,
                                fill: true
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: { legend: { display: false } },
                            scales: {
                                x: { ticks: { color: '#888', font: { size: 10 } }, grid: { color: '#0f3460' } },
                                y: { ticks: { color: '#888' }, grid: { color: '#0f3460' } }
                            }
                        }
                    });
                });
        }

        // =====================
        // 로그
        // =====================
        function loadLogs(deviceId) {
            fetch('/api/logs/' + deviceId)
                .then(res => res.json())
                .then(function(logs) {
                    var logList = document.getElementById('log-list');
                    logList.innerHTML = '';
                    logs.forEach(function(log) {
                        var div  = document.createElement('div');
                        div.className = 'log-item';
                        var time = log.checkedAt ? log.checkedAt.substring(0, 16).replace('T', ' ') : '-';
                        var ms   = log.responseTime ? log.responseTime + 'ms' : 'timeout';
                        div.innerHTML =
                            '<span class="time">' + time + '</span>' +
                            '<span class="ms ' + log.status + '">' + ms + '</span>' +
                            '<span class="' + log.status + '">' + log.status + '</span>';
                        logList.appendChild(div);
                    });
                });
        }

        // =====================
        // 그룹 목록 로드 (공통)
        // =====================
        function loadGroupSelect(selectedGroupId) {
            fetch('/api/groups')
                .then(res => res.json())
                .then(function(groups) {
                    var select = document.getElementById('input-device-group');
                    select.innerHTML = '<option value="">미분류</option>';
                    groups.forEach(function(g) {
                        var option = document.createElement('option');
                        option.value = g.id;
                        option.textContent = g.name;
                        if (selectedGroupId && selectedGroupId == g.id) {
                            option.selected = true;
                        }
                        select.appendChild(option);
                    });
                });
        }

        // =====================
        // 장비 추가 모달
        // =====================
        function openAddDeviceModal() {
            currentDeviceId = null;
            document.getElementById('modal-device-title').textContent = '장비 추가';
            document.getElementById('btn-device-save').textContent    = '추가';
            document.getElementById('input-device-name').value = '';
            document.getElementById('input-device-url').value  = '';
            document.getElementById('input-device-url').readOnly = false;
            document.getElementById('input-device-desc').value = '';
            loadGroupSelect(null);
            document.getElementById('modal-add-device').classList.add('show');
        }

        // =====================
        // 장비 수정 모달
        // =====================
        function openEditDeviceModal(node) {
            currentDeviceId = node.id.replace('device_', '');
            document.getElementById('modal-device-title').textContent = '장비 수정';
            document.getElementById('btn-device-save').textContent    = '수정';
            document.getElementById('input-device-url').readOnly = true;

            fetch('/api/devices/' + currentDeviceId)
                .then(res => res.json())
                .then(function(device) {
                    document.getElementById('input-device-name').value = device.name;
                    document.getElementById('input-device-url').value  = device.url;
                    document.getElementById('input-device-desc').value = device.description || '';
                    loadGroupSelect(device.group ? device.group.id : null);
                    document.getElementById('modal-add-device').classList.add('show');
                });
        }

        // =====================
        // 장비 저장 (추가/수정 공통)
        // =====================
        function saveDevice() {
            var name    = document.getElementById('input-device-name').value.trim();
            var url     = document.getElementById('input-device-url').value.trim();
            var groupId = document.getElementById('input-device-group').value;
            var desc    = document.getElementById('input-device-desc').value.trim();

            if (!name || !url) { alert('장비명과 URL은 필수입니다.'); return; }

            var body = { name: name, url: url, description: desc };
            if (groupId) body.group = { id: parseInt(groupId) };

            if (currentDeviceId) {
                // 수정 - PUT
                fetch('/api/devices/' + currentDeviceId, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                })
                .then(function() {
                    closeDeviceModal();
                    loadTree();
                });
            } else {
                // 추가 - POST
                fetch('/api/devices', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                })
                .then(function() {
                    closeDeviceModal();
                    loadTree();
                });
            }
        }

        // =====================
        // 장비 삭제
        // =====================
        function deleteDevice(node) {
            var deviceId = node.id.replace('device_', '');
            if (!confirm(node.text + '을(를) 삭제하시겠습니까?')) return;
            fetch('/api/devices/' + deviceId, { method: 'DELETE' })
                .then(function() { loadTree(); });
        }

        // =====================
        // 그룹 추가 모달
        // =====================
        function openAddGroupModal() {
            currentGroupId = null;
            document.getElementById('modal-group-title').textContent = '그룹 추가';
            document.getElementById('btn-group-save').textContent    = '추가';
            document.getElementById('input-group-name').value = '';
            document.getElementById('input-group-desc').value = '';
            document.getElementById('modal-add-group').classList.add('show');
        }

        // =====================
        // 그룹 수정 모달
        // =====================
        function openEditGroupModal(node) {
            currentGroupId = node.id.replace('group_', '');
            document.getElementById('modal-group-title').textContent = '그룹 수정';
            document.getElementById('btn-group-save').textContent    = '수정';

            fetch('/api/groups/' + currentGroupId)
                .then(res => res.json())
                .then(function(group) {
                    document.getElementById('input-group-name').value = group.name;
                    document.getElementById('input-group-desc').value = group.description || '';
                    document.getElementById('modal-add-group').classList.add('show');
                });
        }

        // =====================
        // 그룹 저장 (추가/수정 공통)
        // =====================
        function saveGroup() {
            var name = document.getElementById('input-group-name').value.trim();
            var desc = document.getElementById('input-group-desc').value.trim();

            if (!name) { alert('그룹명은 필수입니다.'); return; }

            var body = { name: name, description: desc };

            if (currentGroupId) {
                // 수정 - PUT
                fetch('/api/groups/' + currentGroupId, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                })
                .then(function() {
                    closeGroupModal();
                    loadTree();
                });
            } else {
                // 추가 - POST
                fetch('/api/groups', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                })
                .then(function() {
                    closeGroupModal();
                    loadTree();
                });
            }
        }

        // =====================
        // 그룹 삭제
        // =====================
        function deleteGroup(node) {
            var groupId = node.id.replace('group_', '');
            if (!confirm(node.text + '을(를) 삭제하시겠습니까?')) return;
            fetch('/api/groups/' + groupId, { method: 'DELETE' })
                .then(function() { loadTree(); });
        }

        // =====================
        // 모달 닫기
        // =====================
        function closeDeviceModal() {
            currentDeviceId = null;
            document.getElementById('input-device-url').readOnly = false;
            document.getElementById('modal-add-device').classList.remove('show');
        }

        function closeGroupModal() {
            currentGroupId = null;
            document.getElementById('modal-add-group').classList.remove('show');
        }
		
		
		
		function loadTopology() {
			fetch('/api/topology')
            .then(res => res.json())
            .then(data => {
				var container = document.getElementById('topology-canvas')
				var nodes = new vis.DataSet(data.nodes);
				var edges = new vis.DataSet(data.edges);
				var options = {
				    physics: {
				        enabled: false
				    }
				};
				var nodeData = {
					nodes: nodes,
					edges: edges
				};
				network = new vis.Network(container, nodeData, options);

				});
		}
    </script>

</body>
</html>