import React from 'react';

const OrderedProducts = ({ cartItems }) => {
    return <>
        <h3>Ordered products</h3>
        {
            cartItems.map(cartItem =>
                <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`div-${cartItem.product.id}`}>
                    <p>
                        <b>{cartItem.product.name}</b>
                    </p>

                    {
                        cartItem.product.description &&
                        <p>
                            <b>Description:</b> {cartItem.product.description}
                        </p>
                    }

                    <p>
                        <b>Quantity:</b> {cartItem.cartItem.quantity}
                    </p>

                    <p>
                        <b>Price/Item:</b> {cartItem.cartItem.pricePerProduct.toFixed(2)}PLN
                    </p>

                    <p>
                        <b>Total price:</b> {cartItem.cartItem.pricePerProduct.toFixed(2) * cartItem.cartItem.quantity} PLN
                    </p>
                </div>
            )
        }
    </>
};

export default OrderedProducts;