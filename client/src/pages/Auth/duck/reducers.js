import types from './types';

const authReducer = (state = null, action) => {
    switch(action.type) {
        case types.LOG_IN: 
            return {
                token: action.item.token,
                tokenExpiry: action.item.tokenExpiry,
                email: action.item.email,
                role: action.item.role
            };
        case types.LOG_OUT:
            return null;
        default:
            return state;
    }
};

export default authReducer;