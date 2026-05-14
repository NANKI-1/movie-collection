/**
 * API 接口封装 - 调用后端 Spring Boot 服务
 */

const API_BASE_URL = 'https://56a3d8bd.r22.cpolar.top';

// 获取 Token
function getToken() {
    return localStorage.getItem('token');
}

// 通用请求方法
async function request(url, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            ...options,
            headers
        });

        const data = await response.json();

        if (data.code === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            // 不自动跳转，让调用方处理
        }

        return data;
    } catch (error) {
        console.error('API请求失败:', error);
        return {
            code: 500,
            message: '网络请求失败，请检查后端服务是否启动'
        };
    }
}

// ==================== 用户相关 API ====================
const userAPI = {
    register: (username, password, email) => {
        return request('/user/register', {
            method: 'POST',
            body: JSON.stringify({ username, password, email })
        });
    },
    login: (username, password) => {
        return request('/user/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    },
    updateProfile: (data) => {
        return request('/user/update', {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }
};

// ==================== 电影相关 API ====================
const movieAPI = {
    getAllMovies: () => {
        return request('/movie/list');
    },
    searchMovies: (params) => {
        const queryString = new URLSearchParams(params).toString();
        return request(`/movie/search?${queryString}`);
    },
    addMovie: (movieData) => {
        return request('/movie/add', {
            method: 'POST',
            body: JSON.stringify(movieData)
        });
    },
    updateMovie: (collectionId, movieData) => {
        return request(`/movie/update/${collectionId}`, {
            method: 'PUT',
            body: JSON.stringify(movieData)
        });
    },
    deleteMovie: (collectionId) => {
        return request(`/movie/delete/${collectionId}`, {
            method: 'DELETE'
        });
    },
};

// ==================== 评论相关 API ====================
const commentAPI = {
    getCommentsByMovie: (movieId) => {
        return request(`/comment/list/${movieId}`);
    },
    getCommentsByTMDBId: (tmdbId) => {
        return request(`/comment/listByTmdb/${tmdbId}`);
    },
    getAverageRating: (movieId) => {
        return request(`/comment/average/${movieId}`);
    },
    getAverageRatingByTMDBId: (tmdbId) => {
        return request(`/comment/averageByTmdb/${tmdbId}`);
    },
    addComment: (commentData) => {
        return request('/comment/add', {
            method: 'POST',
            body: JSON.stringify(commentData)
        });
    },
    addCommentByTMDB: (commentData) => {
        return request('/comment/addByTmdb', {
            method: 'POST',
            body: JSON.stringify(commentData)
        });
    },
    updateComment: (commentId, commentData) => {
        return request(`/comment/update/${commentId}`, {
            method: 'PUT',
            body: JSON.stringify(commentData)
        });
    },
    deleteComment: (commentId) => {
        return request(`/comment/delete/${commentId}`, {
            method: 'DELETE'
        });
    },
    getMyComments: () => {
        return request('/comment/my');
    }
};