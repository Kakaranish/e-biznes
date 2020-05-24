import { combineReducers } from 'redux';
import authReducer from './pages/Auth/duck';
import notifReducer from './pages/Notifications/ducks';

const rootReducer = combineReducers({
    auth: authReducer,
    notifs: notifReducer
});

export default rootReducer;