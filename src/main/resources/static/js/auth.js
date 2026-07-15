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
      window.location.href = '/login.html';
    }
  }).fail(function () {
    window.location.href = '/login.html';
  });
}

function logout() {
  ajaxRequest({
    path: '/auth/logout',
    type: 'POST'
  }).always(function () {
    window.location.href = '/login.html';
  });
}

$(function () {
  const loginForm = $('#login-form');
  if (loginForm.length > 0) {
    loginForm.on('submit', function (e) {
      e.preventDefault();
      const username = $('#username').val().trim();
      const password = $('#password').val();
      const btn = loginForm.find('button[type="submit"]');
      if (!username || !password) {
        showToast('请输入用户名和密码', 'error');
        return;
      }
      btn.prop('disabled', true).text('登录中...');
      ajaxRequest({
        path: '/auth/login',
        type: 'POST',
        data: { username: username, password: password }
      }).done(function (res) {
        if (res && res.code === 200 && res.data) {
          showToast('登录成功', 'success');
          setTimeout(function () {
            window.location.href = '/dashboard.html';
          }, 500);
        }
      }).always(function () {
        btn.prop('disabled', false).text('登录');
      });
    });
  }
});
