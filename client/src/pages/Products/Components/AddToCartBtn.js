import React from 'react';

const AddToCartBtn = ({ onAddToCartCb, productInfo, authenticated }) => {

    const canBeAddedToCart = authenticated && !productInfo.cartItem;

    const inputPlaceholder = canBeAddedToCart
        ? "Quantity to add"
        : "Not available";

    const getBtnLabel = () => {
        if (!canBeAddedToCart) return 'Log in to add to cart';
        else if (productInfo.cartItem) return 'Product already in cart';
        else return 'Add to cart'
    };

    const onAddToCart = event => onAddToCartCb(event);

    return <>
        <form onSubmit={onAddToCart} className="form-inline mb-4">
            <div className="form-group w-100">
                <input name="quantity" type="number" step="1" min="0" max={productInfo.product.quantity} className="form-control form-control"
                    style={{ width: "10%" }} placeholder={inputPlaceholder} disabled={!canBeAddedToCart} required />

                <button type="submit" className="btn btn-primary w-25 mr-2" disabled={!canBeAddedToCart}>
                    {getBtnLabel()}
                </button>
            </div>
        </form>
    </>
};

export default AddToCartBtn;