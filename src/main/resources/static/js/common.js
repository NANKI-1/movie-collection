/**
 * 通用工具函数
 */

// 显示 Toast 提示
function showToast(message, type = 'info') {
  const existingToast = document.querySelector('.toast');
  if (existingToast) existingToast.remove();

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;

  const iconMap = {
    success: '✓',
    error: '✗',
    warning: '⚠',
    info: 'ℹ'
  };

  toast.innerHTML = `<span>${iconMap[type] || 'ℹ'}</span><span>${message}</span>`;
  document.body.appendChild(toast);

  setTimeout(() => toast.classList.add('show'), 10);
  setTimeout(() => {
    toast.classList.remove('show');
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

// 渲染星星评分
function renderStars(rating, size = 'small') {
  const fullStars = Math.floor(rating);
  const halfStar = rating - fullStars >= 0.5;
  const sizeClass = size === 'large' ? 'star-large' : (size === 'small' ? 'star-small' : '');

  let stars = '<div class="stars-container">';

  for (let i = 1; i <= 5; i++) {
    if (i <= fullStars) {
      stars += `<span class="star active ${sizeClass}">★</span>`;
    } else if (i === fullStars + 1 && halfStar) {
      stars += `<span class="star active ${sizeClass}">½</span>`;
    } else {
      stars += `<span class="star ${sizeClass}">☆</span>`;
    }
  }

  stars += '</div>';
  return stars;
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 用户名脱敏
function maskUsername(username) {
  if (!username) return '匿名用户';
  if (username.length <= 2) {
    return username[0] + '*';
  }
  return username[0] + '*'.repeat(username.length - 2) + username[username.length - 1];
}

// HTML 转义
function escapeHtml(text) {
  if (!text) return '';
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

// 获取观影状态样式类
function getWatchStatusClass(status) {
  const statusMap = {
    '想看': '想看',
    '已看': '已看',
    '不感兴趣': '不感兴趣'
  };
  return statusMap[status] || '';
}

// 获取当前用户
function getCurrentUser() {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    return JSON.parse(userStr);
  }
  return null;
}

// 退出登录
function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  showToast('已退出登录', 'success');
  setTimeout(() => {
    window.location.href = 'index.html';
  }, 500);
}