import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import Modal from '../../../components/Modal';
import { isValidUUID, doRequest } from '../../../Utils';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const ProductPage = (props) => {

    const history = useHistory();
    const productId = props.match.params.id;

    const onEdit = () => history.push(`/manage/products/${productId}/edit`);

    const onDelete = async () => {
        const action = async () => axios.delete('/api/products', {
            headers: { 'X-Auth-Token': props.auth.token },
            validateStatus: false,
            data: { id: productId }
        });
        try {
            await doRequest(action);
            setValidationErrors(null);
            history.push('/manage/products');
        } catch (error) {
            if (error === 404) {
                setValidationErrors(["no product with such id"]);
                return;
            }
            alert(`${error} occured`)
        }
    }

    const [validationErrors, setValidationErrors] = useState(null);
    const [state, setState] = useState({ loading: true, product: null });
    useEffect(() => {
        const fetchProduct = async () => {
            const action = async () => axios.get(`/api/products/${productId}`,
                { validateStatus: false });

            try {
                const result = await doRequest(action);
                setState({ loading: false, product: result });
            } catch (error) {
                if (error === 404) setState({ loading: false, product: null });
                alert(`${error} occured`);
            }
        };

        if (isValidUUID(productId)) fetchProduct();
    }, []);

    if (!isValidUUID(productId)) return <h3>Product Id '{productId}' is invalid UUID</h3>
    else if (state.loading) return <></>
    else {
        if (!state.product) return <h3>Product Id '{productId}' does not exist</h3>
        else return <>

            <Product product={state.product} />

            <button type="button" className="btn btn-primary w-25 mr-2" onClick={onEdit}>
                Edit
			</button>

            <Modal title={"Are you sure"}
                btnText={"Delete"}
                btnClasses={"btn btn-danger w-25"}
                modalTitle={"Are you sure?"}
                modalPrimaryBtnText={"Delete"}
                modalPrimaryBtnClasses={"btn btn-danger"}
                onModalPrimaryBtnClick={onDelete}
                modalSecondaryBtnText={"Cancel"}
                modalSecondaryBtnClasses={"btn btn-secondary"}
            />

            {
                validationErrors &&
                <div className="col-12 mt-2">
                    <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                        Validation errors
                        </p>
                    <ul style={{ paddingTop: "0" }, { marginTop: "0px" }}>
                        {
                            validationErrors.map((error, i) => {
                                return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                            })
                        }
                    </ul>
                </div>
            }
        </>
    }
};

const Product = (props) => {
    const product = props.product;
    return <>
        <h3>Product {product.product.id}</h3>

        <p>
            <b>Name:</b> {product.product.name}
        </p>

        <p>
            <b>Description:</b> {product.product.description}
        </p>

        <p>
            <b>Price:</b> {product.product.price.toFixed(2)}
        </p>

        <p>
            <b>Available quantity:</b> {product.product.quantity}
        </p>

        <p>
            <b>Category:</b> {product.category?.name ?? 'None'}
        </p>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(ProductPage);