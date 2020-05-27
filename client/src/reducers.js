import { combineReducers } from 'redux';
import authReducer from './pages/Auth/duck';
import notifReducer from './pages/Notifications/ducks';
import cartItemsReducer from './pages/Cart/ducks';

const rootReducer = combineReducers({
    auth: authReducer,
    notifs: notifReducer,
    cartItems: cartItemsReducer
});

export default rootReducer;