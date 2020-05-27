import React from 'react';
import { Link } from 'react-router-dom';
import cartIcon from '../../src/assets/img/cart.svg';
import AwareComponentBuilder from '../common/AwareComponentBuilder';

const Cart = (props) => {
    console.log(props);
    console.log(props.cartItems);
    return <>
        <Link to={{ pathname: '/cart' }}>
            <img src={cartIcon} style={{ width: "25px", opacity: "0.4", paddingTop: "7px" }} />

            {
                props.cartItems?.length > 0 &&
                <span className="badge badge-danger">
                    {props.cartItems.length}
                </span>
            }
        </Link>
    </>
};

export default new AwareComponentBuilder()
    .withCartAwareness()
    .build(Cart);