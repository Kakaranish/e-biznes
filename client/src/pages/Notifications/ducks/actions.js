import types from './types';

export const setNotifs = notifs => ({
    type: types.SET_NOTIFS, notifs
});

export const clearNotifs = () => ({
    type: types.CLEAR_NOTIFS
});

export default {
    setNotifs,
    clearNotifs
};