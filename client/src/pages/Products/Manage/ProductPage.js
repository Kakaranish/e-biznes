import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import Modal from '../../../components/Modal';
import { isValidUUID } from '../../../common';

const ProductPage = (props) => {

    const history = useHistory();
    const productId = props.match.params.id;

    const onEdit = () => history.push(`/manage/products/${productId}/edit`);
    const onDelete = async () => {
        const result = await axios.delete('/api/products',
            { validateStatus: false, data: { id: productId } });
        if (result.status === 500) {
            alert('Some error occured');
            console.log(result);
            return;
        }
        if (result.status === 404) {
            const validationErrors = result.data.obj.map(e => e.msg[0]);
            setValidationErrors(validationErrors);
            return;
        }
        alert('Removed');
        setValidationErrors(null);
        history.push('/manage/products');
    }
    
    const [validationErrors, setValidationErrors] = useState(null);
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

            <p>
                <b>Description:</b> {state.product.product.description}
            </p>

            <p>
                <b>Price:</b> {state.product.product.price.toFixed(2)}
            </p>

            <p>
                <b>Available quantity:</b> {state.product.product.quantity}
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

export default ProductPage;