import { combineReducers } from 'redux';
import authReducer from './pages/Auth/duck';

const rootReducer = combineReducers({
    auth: authReducer
});

export default rootReducer;