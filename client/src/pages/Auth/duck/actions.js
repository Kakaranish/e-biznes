import types from './types';

export const logIn = item => ({
    type: types.LOG_IN, item
});

export const logOut = () => ({
    type: types.LOG_OUT
});

export default {
    logIn,
    logOut
};