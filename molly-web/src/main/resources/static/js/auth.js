function checkLogin(callback) {
  ajaxRequest({
    path: '/auth/info',
    type: 'GET'
  }).done(function (res) {
    if (res && res.code === 200 && res.data) {
      setUserInfo(res.data);
      if (typeof callback === 'function') {
        callback(res.data);
      }
    } else {
      clearUserInfo();
      window.location.href = '/login';
    }
  }).fail(function () {
    clearUserInfo();
    window.location.href = '/login';
  });
}

function logout() {
  ajaxRequest({
    path: '/auth/logout',
    type: 'POST'
  }).done(function () {
    clearUserInfo();
    window.location.href = '/login';
  });
}
