import types from './types';

const cartItemsReducer = (state = [], action) => {
    switch (action.type) {
        case types.CART_SET_ITEMS:
            return [...action.cartItems];
        case types.CART_ADD:
            return [...state, action.productId] ;
        case types.CART_REMOVE:
            return state.filter(x => x !== action.productId);
        case types.CART_CLEAR:
            return [];
        default:
            return state;
    }
};

export default cartItemsReducer;