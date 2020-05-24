import types from './types';

const notifReducer = (state = [], action) => {
    switch (action.type) {
        case types.SET_NOTIFS:
            return action.notifs;
        case types.CLEAR_NOTIFS:
            return [];
        default:
            return state;
    }
};

export default notifReducer;