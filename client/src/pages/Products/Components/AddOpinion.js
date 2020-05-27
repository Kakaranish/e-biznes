import React from 'react';
import { getFormDataJsonFromEvent, doRequest } from '../../../Utils';
import axios from 'axios';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';

const AddOpinion = (props) => {

    const productId = props.productId;

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        formData.productId = productId;

        try {
            const action = async () => axios.post('/api/opinions', formData, {
                validateStatus: false,
                headers: { 'X-Auth-Token': props.auth.token }
            });
            await doRequest(action);
        } catch (error) {
            alert(`${error} error occured`);
        }

        props.callback();
    };

    return <>
        <h3>Add opinion</h3>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <textarea name="content" className="form-control w-50" rows="3" required>
                </textarea>
            </div>
            <button className="btn btn-primary px-5">
                Add
            </button>
        </form>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(AddOpinion);