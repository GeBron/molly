const API_BASE = '/api';

let currentUserInfo = null;
const USER_INFO_KEY = 'molly_user_info';

function escapeHtml(text) {
  if (text == null) return '';
  return String(text)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

function getUserInfo() {
  if (currentUserInfo) {
    return currentUserInfo;
  }
  try {
    const cached = sessionStorage.getItem(USER_INFO_KEY);
    return cached ? JSON.parse(cached) : null;
  } catch (e) {
    return null;
  }
}

function setUserInfo(info) {
  currentUserInfo = info;
  try {
    if (info) {
      sessionStorage.setItem(USER_INFO_KEY, JSON.stringify(info));
    } else {
      sessionStorage.removeItem(USER_INFO_KEY);
    }
  } catch (e) {}
}

function clearUserInfo() {
  currentUserInfo = null;
  try {
    sessionStorage.removeItem(USER_INFO_KEY);
  } catch (e) {}
}

function hasPermission(perm) {
  const info = getUserInfo();
  if (!info || !info.permissions) {
    return false;
  }
  return info.permissions.includes(perm);
}

function showToast(message, type) {
  type = type || 'info';
  const bgClass = type === 'success' ? 'bg-success' : type === 'error' ? 'bg-danger' : 'bg-info';
  const toastHtml =
    '<div class="toast align-items-center ' + bgClass + ' text-white border-0" role="alert" aria-live="assertive" aria-atomic="true">' +
    '  <div class="d-flex">' +
    '    <div class="toast-body">' + escapeHtml(message) + '</div>' +
    '    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>' +
    '  </div>' +
    '</div>';
  let container = $('#toast-container');
  if (container.length === 0) {
    container = $('<div id="toast-container" class="toast-container position-fixed top-0 end-0 p-3"></div>');
    $('body').append(container);
  }
  const toastEl = $(toastHtml);
  container.append(toastEl);
  const toast = new bootstrap.Toast(toastEl[0], { delay: 3000 });
  toast.show();
  toastEl.on('hidden.bs.toast', function () {
    toastEl.remove();
  });
}

function confirmDialog(message, callback) {
  const modalHtml =
    '<div class="modal fade" id="common-confirm-modal" tabindex="-1" aria-hidden="true">' +
    '  <div class="modal-dialog modal-dialog-centered">' +
    '    <div class="modal-content">' +
    '      <div class="modal-header">' +
    '        <h5 class="modal-title">确认</h5>' +
    '        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
    '      </div>' +
    '      <div class="modal-body">' + escapeHtml(message) + '</div>' +
    '      <div class="modal-footer">' +
    '        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>' +
    '        <button type="button" class="btn btn-danger" id="common-confirm-ok">确定</button>' +
    '      </div>' +
    '    </div>' +
    '  </div>' +
    '</div>';
  $('body').append(modalHtml);
  const modal = new bootstrap.Modal($('#common-confirm-modal')[0]);
  modal.show();
  $('#common-confirm-ok').one('click', function () {
    $(this).prop('disabled', true).text('处理中...');
    modal.hide();
    if (typeof callback === 'function') {
      callback();
    }
  });
  $('#common-confirm-modal').on('hidden.bs.modal', function () {
    $(this).remove();
  });
}

function formatDateTime(isoString) {
  if (!isoString) return '-';
  const date = new Date(isoString);
  if (isNaN(date.getTime())) return isoString;
  const pad = (n) => (n < 10 ? '0' + n : n);
  return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate()) + ' ' +
    pad(date.getHours()) + ':' + pad(date.getMinutes()) + ':' + pad(date.getSeconds());
}

function getCsrfToken() {
  const meta = document.querySelector('meta[name="_csrf"]');
  if (meta && meta.content) {
    return meta.content;
  }
  const input = document.querySelector('input[name="_csrf"]');
  if (input && input.value) {
    return input.value;
  }
  const match = document.cookie.match(new RegExp('(^| )XSRF-TOKEN=([^;]+)'));
  return match ? decodeURIComponent(match[2]) : null;
}

function ajaxRequest(options) {
  const settings = $.extend({
    url: API_BASE + options.path,
    type: 'GET',
    contentType: 'application/json',
    dataType: 'json',
    xhrFields: { withCredentials: true }
  }, options);

  if (settings.type === 'GET') {
    delete settings.contentType;
  } else if (settings.data && settings.contentType === 'application/json' && typeof settings.data !== 'string') {
    settings.data = JSON.stringify(settings.data);
  }

  const csrfToken = getCsrfToken();
  if (csrfToken) {
    settings.headers = $.extend(settings.headers || {}, {
      'X-XSRF-TOKEN': csrfToken
    });
  }

  return $.ajax(settings)
    .done(function (res) {
      if (res && res.code !== 200) {
        showToast(res.message || '操作失败', 'error');
        return $.Deferred().reject(res).promise();
      }
      return res;
    })
    .fail(function (xhr) {
      if (xhr.status === 401) {
        showToast('登录已过期，请重新登录', 'error');
        setTimeout(function () {
          window.location.href = '/login';
        }, 1500);
        return;
      }
      if (xhr.status === 403) {
        showToast('没有权限执行该操作', 'error');
        return;
      }
      const res = xhr.responseJSON;
      showToast(res && res.message ? res.message : '请求失败', 'error');
    });
}

function setButtonLoading(btn, loading) {
  if (loading) {
    btn.data('original-text', btn.text());
    btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> 处理中...');
  } else {
    btn.prop('disabled', false).text(btn.data('original-text') || btn.text());
  }
}

function initPage() {
  // 页面通用初始化可在此处扩展；权限控制已由 Thymeleaf 服务端渲染
}

$(document).ajaxError(function (event, xhr) {
  if (xhr.status === 401) {
    window.location.href = '/login';
  }
});
