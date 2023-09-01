import SavePoiComponent from "../common/SavePoiComponent.js";
import RouteLayerGroup from "../RouteLayerGroup.js";
import {
    handleChangeRouteSearchInput,
    handleClickRouteToggleBtn,
    handleCloseWaypointButton, handleOnClickOpenRouteSaveForm,
    handleSummitRouteSearchForm
} from "./handle.js";

(function () {
        window.customElements.define('save-modal-element', SavePoiComponent);

        let routeLayerGroup = new RouteLayerGroup(map_instance);

        handleSummitRouteSearchForm(routeLayerGroup);
        handleCloseWaypointButton();
        handleChangeRouteSearchInput();
        handleClickRouteToggleBtn(routeLayerGroup);

        handleOnClickOpenRouteSaveForm();

    }
)();
