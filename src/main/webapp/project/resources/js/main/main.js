const mainFn = {
	forwardMenu: (code, copSeqNo, isMngTable) => {
		let url = '';
		isMngTable === undefined ? isMngTable = 0 : isMngTable;

		/*
		* code : 접근 서비스
		* copSeqNo : 캠페인 권한 보유 아이디 시퀀스(권한 미보유 시 0)
		* isMngTable : 이벤트/아라쇼 권한 보유 아이디 여부 (권한 미보유 시 0)
		*
		* 라이브 : 미개발
		* */
		if(code === 'live') {
			alert('서비스 준비 중 입니다.');
			return false;
		} else {
			if(code === 'campaign') {
				if(copSeqNo > 0) {
					url = '/partners/campaign';
				} else {
					alert('접근 권한이 없습니다.');
					return false;
				}
			} else if(code === 'event') {
				if(isMngTable > 0) {
					url = '/partners/event';
				} else {
					alert('접근 권한이 없습니다.');
					return false;
				}
			}

			window.location.href = url;
		}
	}
};

const mainApi = {

};

const mainDraw = {

};