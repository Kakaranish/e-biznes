import axios from 'axios';

export const verifyAuthState = async () => {
    const authStr = localStorage.getItem('auth');
    let authObj;
    try {
        authObj = JSON.parse(authStr);
    } catch (error) { authObj = null; }

    if (!authObj) return null;
    if (!authObj?.token) {
        localStorage.removeItem('auth');
        return null;
    }

    if (!authObj.tokenExpiry || !authObj.role || !authObj.email
        || Date.now() > authObj.tokenExpiry) {
        const result = await axios.post('/auth/verify', {}, {
            headers: { 'X-Auth-Token': authObj.token },
            validateStatus: false
        });
        if (result.state !== 200) {
            localStorage.removeItem('auth');
            return false;
        }
        const localStorageItem = {
            token: result.data.token,
            tokenExpiry: parseInt(result.data.tokenExpiry),
            email: result.data.email,
            role: result.data.role
        };
        authObj = localStorageItem;

        localStorage.setItem('auth', JSON.stringify(localStorageItem));
    }

    return {
        email: authObj.email,
        role: authObj.role
    };
}

export const getToken = () => {
    try {
        const authObj = JSON.parse(localStorage.getItem('auth'));
        return authObj.token;
    } catch (error) { return null; }
};