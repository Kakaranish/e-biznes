import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import Modal from '../../Modal';
import { isValidUUID } from '../../common';

const ProductPage = (props) => {

    const history = useHistory();
    const productId = props.match.params.id;

    const onEdit = () => history.push(`/products/${productId}/edit`);

    const [state, setState] = useState({ loading: true, product: null });
    useEffect(() => {
        const fetchProduct = async () => {
            const result = await axios.get(`/api/products/${productId}`,
                { validateStatus: false });

            if (result.status === 404) {
                setState({ loading: false, product: null });
                return;
            }
            if (result.status !== 200) {
                alert('Some error occured');
                return;
            }
            setState({ loading: false, product: result.data });
        };

        if (isValidUUID(productId)) fetchProduct();
    }, []);

    if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else {
        if (!state.product) return <h3>Product Id '{productId}' does not exist</h3>
        else return <>
            <h3>Product {state.product.product.id}</h3>

            <p>
                <b>Name:</b> {state.product.product.name}
            </p>

            {
                state.product.product.description &&
                <p>
                    <b>Name:</b> {state.product.product.description}
                </p>
            }

            <p>
                <b>Price:</b> {state.product.product.price}
            </p>

            <p>
                <b>Quantity:</b> {state.product.product.quantity}
            </p>

            <p>
                <b>Category:</b> {state.product.category?.name ?? 'None'}
            </p>

            <button type="button" className="btn btn-primary w-25 mr-2" onClick={onEdit}>
                Edit
			</button>

            <Modal title={"Are you sure"}
                btnText={"Delete"}
                btnClasses={"btn btn-danger w-25"}
                modalTitle={"Are you sure?"}
                modalPrimaryBtnText={"Delete"}
                modalPrimaryBtnClasses={"btn btn-danger"}
                onModalPrimaryBtnClick={() => { }}
                modalSecondaryBtnText={"Cancel"}
                modalSecondaryBtnClasses={"btn btn-secondary"}
            />
        </>
    }
};

export default ProductPage;