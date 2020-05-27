import types from './types';

export const setCartItems = cartItems => ({
    type: types.CART_SET_ITEMS, cartItems
});

export const addToCart = productId => ({
    type: types.CART_ADD, productId
});

export const removeFromCart = productId => ({
    type: types.CART_REMOVE, productId
});

export const clearCart = () => ({
    type: types.CART_CLEAR
});

export default {
    setCartItems,
    addToCart,
    removeFromCart,
    clearCart
};